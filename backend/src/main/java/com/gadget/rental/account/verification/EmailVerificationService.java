package com.gadget.rental.account.verification;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import jakarta.mail.MessagingException;

import com.gadget.rental.account.admin.AdminAccountModel;
import com.gadget.rental.account.admin.AdminAccountRepository;
import com.gadget.rental.account.client.ClientAccountModel;
import com.gadget.rental.account.client.ClientAccountRepository;
import com.gadget.rental.exception.AdminAccountLimitExceededException;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailAlreadyVerifiedException;
import com.gadget.rental.exception.EmailVerificationExpiredException;
import com.gadget.rental.exception.EmailVerificationInProgressException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationResendTooSoonException;
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
                Optional<ClientAccountModel> clientExistingAccount = clientAccountRepository
                        .findClientAccountByEmail(emailDTO.email());

                if (clientExistingAccount.isPresent()) {
                    throw new EmailAlreadyBoundException("This email is linked to another account.");
                }

                Optional<EmailVerificationModel> clientExistingVerification = emailVerificationRepository
                        .findEmailVerificationByEmail(emailDTO.email());

                if (clientExistingVerification.isPresent()) {
                    throw new EmailVerificationInProgressException("A verification for this email is in progress.");
                }

                EmailVerificationModel emailVerification = createEmailVerificationUtility(emailDTO);
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

                if (accountNum > 2) {
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
                    throw new EmailVerificationInProgressException("A verification for this email is in progress.");
                }

                EmailVerificationModel emailVerification = createEmailVerificationUtility(emailDTO);
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
        String verificationCode = "";
        Optional<EmailVerificationModel> matchedEmail = emailVerificationRepository
                .findEmailVerificationByEmail(emailDTO.email());

        EmailVerificationModel emailVerificationModel = matchedEmail
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (ZonedDateTime.now(ZoneId.of(emailVerificationModel.getTimezone()))
                .isAfter(matchedEmail.get().getExpiry()
                        .withZoneSameInstant(ZoneId.of(emailVerificationModel.getTimezone())))) {
            throw new EmailVerificationExpiredException("The email verification has expired.");
        }

        if (ZonedDateTime.now(ZoneId.of(emailVerificationModel.getTimezone()))
                .isBefore(emailVerificationModel.getNextValidCodeResendDate()
                        .withZoneSameInstant(ZoneId.of(emailVerificationModel.getTimezone())))) {
            throw new EmailVerificationResendTooSoonException(
                    "Please wait before requesting a new email verification code.");
        }

        emailVerificationModel
                .setNextValidCodeResendDate(
                        emailVerificationModel.getNextValidCodeResendDate().plusMinutes(1l));
        verificationCode = emailVerificationModel.getCode();

        switch (type) {
            case EmailVerificationType.CLIENT -> {
                try {
                    emailSenderService.sendClientVerificationCode(emailVerificationModel.getEmail(),
                            emailVerificationModel.getCode());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            case EmailVerificationType.ADMIN -> {
                try {
                    emailSenderService.sendAdminVerificationCode(emailVerificationModel.getCode());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            default -> {
            }
        }

        return verificationCode;
    }

    public String verifyVerification(EmailVerificationDTO emailVerificationDTO) {

        Optional<EmailVerificationModel> matchedEmail = emailVerificationRepository
                .findEmailVerificationByEmail(emailVerificationDTO.email());

        EmailVerificationModel emailModel = matchedEmail
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not linked to any pending registration."));

        if (emailModel.isVerified()) {
            throw new EmailAlreadyVerifiedException("This email has already been verified.");
        }

        if (emailModel.getCode().compareTo(emailVerificationDTO.code()) != 0) {
            throw new InvalidEmailVerificationCodeException("The email verification code entered is incorrect.");
        }

        if (ZonedDateTime.now(ZoneId.of(emailModel.getTimezone()))
                .isAfter(emailModel.getExpiry().withZoneSameInstant(ZoneId.of(emailModel.getTimezone())))) {
            throw new EmailVerificationExpiredException("The email verification has expired.");
        }

        String token = UUID.randomUUID().toString();
        emailVerificationRepository.updateEmailVerificationToken(token, emailModel.getEmail());
        emailVerificationRepository.updateEmailVerificationIsVerified(true, emailModel.getEmail());

        return token;
    }

    private EmailVerificationModel createEmailVerificationUtility(EmailDTO emailDTO) {
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
        emailVerificationRepository
                .save(emailVerification);
        return emailVerification;
    }
}
