package com.gadget.rental.account.verification;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import jakarta.mail.MessagingException;

import com.gadget.rental.account.admin.AdminAccountModel;
import com.gadget.rental.account.admin.AdminAccountRepository;
import com.gadget.rental.account.client.ClientAccountRepository;
import com.gadget.rental.exception.AdminAccountLimitExceededException;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailAlreadyVerifiedException;
import com.gadget.rental.exception.EmailVerificationAttemptLimitReachedException;
import com.gadget.rental.exception.EmailVerificationExpiredException;
import com.gadget.rental.exception.EmailVerificationInProgressException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationResendTooSoonException;
import com.gadget.rental.exception.EmailVerificationRoleMismatchException;
import com.gadget.rental.exception.InvalidEmailVerificationCodeException;

import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final ClientAccountRepository clientAccountRepository;
    private final AdminAccountRepository adminAccountRepository;
    private final EmailSenderService emailSenderService;

    EmailVerificationService(EmailVerificationRepository emailVerificationRepository,
            ClientAccountRepository clientAccountRepository, EmailSenderService emailSenderService,
            AdminAccountRepository adminAccountRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.clientAccountRepository = clientAccountRepository;
        this.emailSenderService = emailSenderService;
        this.adminAccountRepository = adminAccountRepository;
    }

    public String createVerification(EmailDTO emailDTO, EmailVerificationType type) {
        String verificationCode = "";

        switch (type) {
            case EmailVerificationType.CLIENT -> {
                clientAccountRepository
                        .findClientAccountByEmail(emailDTO.email()).ifPresent((_) -> {
                            throw new EmailAlreadyBoundException("This email is linked to another account.");
                        });

                Optional<EmailVerificationModel> clientExistingVerification = emailVerificationRepository
                        .findEmailVerificationByEmail(emailDTO.email());

                if (clientExistingVerification.isPresent()) {
                    if (clientExistingVerification.get().isTimeExpired()
                            || clientExistingVerification.get().isAttemptsExceeded()) {
                        throw new EmailVerificationExpiredException("The email verification has expired.");
                    }

                    if (clientExistingVerification.get().isClientReRegistrationCooldownExpired()) {
                        emailVerificationRepository
                                .deleteExpiredEmailVerificationByEmail(clientExistingVerification.get().getEmail());
                    }
                    throw new EmailVerificationInProgressException("A verification for this email is in progress.");
                }

                EmailVerificationModel emailVerification = createEmailVerificationUtility(emailDTO, type);
                verificationCode = emailVerification.getCode();

                try {
                    emailSenderService.sendClientVerificationCode(emailVerification.getEmail(),
                            emailVerification.getCode());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            case EmailVerificationType.ADMIN -> {
                long accountNum = adminAccountRepository.count();

                if (accountNum > 1) {
                    throw new AdminAccountLimitExceededException(
                            "Admin account limit reached. No more admin accounts can be created.");
                }

                Optional<AdminAccountModel> adminExistingAccount = adminAccountRepository
                        .findAdminAccountByEmail(emailDTO.email());

                if (adminExistingAccount.isPresent()) {
                    throw new EmailAlreadyBoundException("This email is already an admin.");
                }

                Optional<EmailVerificationModel> adminExistingVerification = emailVerificationRepository
                        .findEmailVerificationByEmail(emailDTO.email());

                if (adminExistingVerification.isPresent()) {
                    if (adminExistingVerification.get().isTimeExpired()
                            || adminExistingVerification.get().isAttemptsExceeded()) {
                        throw new EmailVerificationExpiredException("The email verification has expired.");
                    }

                    if (adminExistingVerification.get().isAdminReRegistrationCooldownExpired()) {
                        emailVerificationRepository
                                .deleteExpiredEmailVerificationByEmail(adminExistingVerification.get().getEmail());
                    }

                    throw new EmailVerificationInProgressException("A verification for this email is in progress.");
                }

                EmailVerificationModel emailVerification = createEmailVerificationUtility(emailDTO, type);
                verificationCode = emailVerification.getCode();

                try {
                    emailSenderService.sendAdminVerificationCode(emailVerification.getCode());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            default -> {
            }
        }

        return verificationCode;
    }

    public String resendVerification(EmailDTO emailDTO, EmailVerificationType type) {
        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(emailDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (matchingEmail.isVerified()) {
            throw new EmailAlreadyVerifiedException("This email has already been verified.");
        }

        if (matchingEmail.isTimeExpired() || matchingEmail.isAttemptsExceeded()) {
            throw new EmailVerificationExpiredException("The email verification has expired.");
        }

        if (!matchingEmail.isEmailResendCooldownExpired()) {
            throw new EmailVerificationResendTooSoonException(
                    "Please wait before requesting a new email verification code.");
        }

        String verificationCode = "";
        verificationCode = EmailCodeGenerator.generateVerificationCode();
        matchingEmail
                .setNextValidCodeResendDate(
                        matchingEmail.getNextValidCodeResendDate().plusMinutes(1l));

        emailVerificationRepository.updateEmailVerificationCode(verificationCode, matchingEmail.getEmail());

        switch (type) {
            case EmailVerificationType.CLIENT -> {
                try {
                    emailSenderService.sendClientVerificationCode(matchingEmail.getEmail(),
                            verificationCode);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            case EmailVerificationType.ADMIN -> {
                try {
                    emailSenderService.sendAdminVerificationCode(verificationCode);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            default -> {
            }
        }

        return verificationCode;
    }

    public String verifyVerification(EmailVerificationDTO emailVerificationDTO, EmailVerificationType type) {

        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(emailVerificationDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not linked to any pending registration."));

        if (!matchingEmail.isAccountTypeMatched(type)) {
            throw new EmailVerificationRoleMismatchException(
                    "Role provided during email verification does not match the expected role.");
        }

        if (matchingEmail.isVerified()) {
            throw new EmailAlreadyVerifiedException("This email has already been verified.");
        }

        if (matchingEmail.isAttemptsExceeded()) {
            throw new EmailVerificationAttemptLimitReachedException("Max email verification attempt has reached!");
        }

        emailVerificationRepository.updateEmailVerificationAttemptCount(matchingEmail.getAttemptCount() + 1,
                matchingEmail.getEmail());

        if (!matchingEmail.isVerificationCodeMatched(emailVerificationDTO.code())) {
            throw new InvalidEmailVerificationCodeException("The email verification code entered is incorrect.");
        }

        if (matchingEmail.isTimeExpired()) {
            throw new EmailVerificationExpiredException("The email verification has expired.");
        }

        String token = UUID.randomUUID().toString();
        emailVerificationRepository.updateEmailVerificationToken(token, matchingEmail.getEmail());
        emailVerificationRepository.updateEmailVerificationIsVerified(true, matchingEmail.getEmail());

        return token;
    }

    private EmailVerificationModel createEmailVerificationUtility(EmailDTO emailDTO, EmailVerificationType type) {
        String verificationCode = EmailCodeGenerator.generateVerificationCode();
        EmailVerificationModel emailVerification = new EmailVerificationModel();
        emailVerification.setEmail(
                emailDTO.email());
        emailVerification.setCode(verificationCode);
        emailVerification.setExpiry(
                ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(5l));
        emailVerification.setTimezone(
                emailDTO.timezone());
        emailVerification.setNextValidCodeResendDate(ZonedDateTime
                .now(ZoneId.of("Z")).plusMinutes(1l));
        emailVerification.setAccountType(type);
        emailVerificationRepository
                .save(emailVerification);
        return emailVerification;
    }
}
