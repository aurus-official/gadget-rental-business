package com.gadget.rental.auth.verification;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import jakarta.mail.MessagingException;

import com.gadget.rental.account.admin.AdminAccountRepository;
import com.gadget.rental.account.client.ClientAccountRepository;
import com.gadget.rental.exception.AdminAccountLimitExceededException;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailAlreadyVerifiedException;
import com.gadget.rental.exception.EmailVerificationAttemptLimitReachedException;
import com.gadget.rental.exception.EmailVerificationExpiredException;
import com.gadget.rental.exception.EmailVerificationInProgressException;
import com.gadget.rental.exception.EmailVerificationRequestNotFoundException;
import com.gadget.rental.exception.EmailVerificationResendTooSoonException;
import com.gadget.rental.exception.EmailVerificationRoleMismatchException;
import com.gadget.rental.exception.InvalidEmailVerificationCodeException;
import com.gadget.rental.shared.AccountType;
import com.gadget.rental.shared.EmailSenderService;

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

    public EmailVerificationResponseDTO createVerification(EmailDTO emailDTO, AccountType type) {
        EmailVerificationModel emailVerification = null;
        clientAccountRepository
                .findClientAccountByEmail(emailDTO.email()).ifPresent((_) -> {
                    throw new EmailAlreadyBoundException("This email is linked to another account.");
                });
        adminAccountRepository
                .findAdminAccountByEmail(emailDTO.email()).ifPresent((_) -> {
                    throw new EmailAlreadyBoundException("This email is linked to another account.");
                });

        switch (type) {
            case AccountType.CLIENT -> {

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

                emailVerification = createEmailVerificationUtility(emailDTO, type);

                try {
                    emailSenderService.sendClientVerificationCode(emailVerification.getEmail(),
                            emailVerification.getCode());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            case AccountType.ADMIN -> {
                long accountNum = adminAccountRepository.count();

                if (accountNum >= 1) {
                    throw new AdminAccountLimitExceededException(
                            "Admin account limit reached. No more admin accounts can be created.");
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

                emailVerification = createEmailVerificationUtility(emailDTO, type);

                try {
                    emailSenderService.sendAdminVerificationCode(emailVerification.getCode());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            default -> {
            }
        }

        EmailVerificationResponseDTO emailVerificationResponseDTO = new EmailVerificationResponseDTO(
                emailVerification.getEmail(), emailVerification.getCodeResendAvailableAt(),
                emailVerification.getValidUntil());

        return emailVerificationResponseDTO;
    }

    public EmailVerificationResponseDTO resendVerification(EmailDTO emailDTO, AccountType type) {
        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(emailDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotFoundException(
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
                .setCodeResendAvailableAt(
                        matchingEmail.getCodeResendAvailableAt().plusMinutes(1l));

        emailVerificationRepository.updateEmailVerificationCode(verificationCode, matchingEmail.getEmail());

        switch (type) {
            case AccountType.CLIENT -> {
                try {
                    emailSenderService.sendClientVerificationCode(matchingEmail.getEmail(),
                            verificationCode);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            case AccountType.ADMIN -> {
                try {
                    emailSenderService.sendAdminVerificationCode(verificationCode);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            default -> {
            }
        }

        EmailVerificationResponseDTO emailVerificationResponseDTO = new EmailVerificationResponseDTO(
                matchingEmail.getEmail(), matchingEmail.getCodeResendAvailableAt(),
                matchingEmail.getValidUntil());

        return emailVerificationResponseDTO;
    }

    public String verifyVerification(EmailVerificationRequestDTO emailVerificationDTO, AccountType type) {

        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(emailVerificationDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotFoundException(
                        "Email is not linked to any registration."));

        if (!matchingEmail.isAccountTypeMatched(type)) {
            throw new EmailVerificationRoleMismatchException(
                    "Role provided during email verification does not match the expected role.");
        }

        if (matchingEmail.isVerified()) {
            throw new EmailAlreadyVerifiedException("Email has already been verified.");
        }

        if (matchingEmail.isAttemptsExceeded()) {
            throw new EmailVerificationAttemptLimitReachedException("Max attempt has reached!");
        }

        emailVerificationRepository.updateEmailVerificationAttemptCount(matchingEmail.getAttemptCount() + 1,
                matchingEmail.getEmail());

        if (!matchingEmail.isVerificationCodeMatched(emailVerificationDTO.code())) {
            throw new InvalidEmailVerificationCodeException("The code is incorrect.");
        }

        if (matchingEmail.isTimeExpired()) {
            throw new EmailVerificationExpiredException("The code has expired.");
        }

        String token = UUID.randomUUID().toString();
        emailVerificationRepository.updateEmailVerificationToken(token, matchingEmail.getEmail());
        emailVerificationRepository.updateEmailVerificationIsVerified(true, matchingEmail.getEmail());

        return token;
    }

    private EmailVerificationModel createEmailVerificationUtility(EmailDTO emailDTO, AccountType type) {
        String verificationCode = EmailCodeGenerator.generateVerificationCode();
        EmailVerificationModel emailVerification = new EmailVerificationModel();
        emailVerification.setEmail(
                emailDTO.email());
        emailVerification.setCode(verificationCode);
        emailVerification.setValidUntil(
                ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(2l));
        emailVerification.setTimezone(
                emailDTO.timezone());
        emailVerification.setCodeResendAvailableAt(ZonedDateTime
                .now(ZoneId.of("Z")).plusMinutes(1l));
        emailVerification.setAccountType(type);
        emailVerificationRepository
                .save(emailVerification);
        return emailVerification;
    }
}
