package com.gadget.rental.email;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.gadget.rental.client.ClientModel;
import com.gadget.rental.client.ClientRepository;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailAlreadyVerifiedException;
import com.gadget.rental.exception.EmailVerificationExpiredException;
import com.gadget.rental.exception.EmailVerificationInProgressException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationResendTooSoonException;
import com.gadget.rental.exception.InvalidEmailVerificationCodeException;

import jakarta.mail.MessagingException;

import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final ClientRepository clientRepository;
    private final EmailSenderService emailSenderService;

    EmailVerificationService(EmailVerificationRepository emailVerificationRepository,
            ClientRepository clientRepository, EmailSenderService emailSenderService) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.clientRepository = clientRepository;
        this.emailSenderService = emailSenderService;
    }

    public String createVerificationCodeModel(EmailDTO emailDTO) {
        Optional<ClientModel> boundClientModelToEmail = clientRepository.getClientModelByEmail(emailDTO.email());
        Optional<EmailVerificationModel> matchedEmailVerification = emailVerificationRepository
                .getEmailVerificationModelByEmail(emailDTO.email());

        if (boundClientModelToEmail.isPresent()) {
            throw new EmailAlreadyBoundException("This email is linked to another account.");
        }

        if (matchedEmailVerification.isPresent()) {
            throw new EmailVerificationInProgressException("A verification for this email is in progress.");
        }

        String verificationCode = EmailCodeGenerator.generateVerificationCode();
        EmailVerificationModel emailVerificationModel = new EmailVerificationModel();
        emailVerificationModel.setEmail(emailDTO.email());
        emailVerificationModel.setCode(verificationCode);
        emailVerificationModel.setExpiry(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(3l));
        emailVerificationModel.setTimezone(emailDTO.timezone());
        emailVerificationModel.setNextValidCodeResentDate(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(1l));
        emailVerificationRepository.save(emailVerificationModel);

        try {
            emailSenderService.sendVerificationCode(emailVerificationModel.getEmail(), verificationCode);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return verificationCode;
    }

    public String resendVerificationCodeModel(EmailDTO emailDTO) {
        Optional<EmailVerificationModel> matchedEmail = emailVerificationRepository
                .getEmailVerificationModelByEmail(emailDTO.email());

        EmailVerificationModel emailModel = matchedEmail
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (ZonedDateTime.now(ZoneId.of(emailModel.getTimezone()))
                .isAfter(matchedEmail.get().getExpiry().withZoneSameInstant(ZoneId.of(emailModel.getTimezone())))) {
            throw new EmailVerificationExpiredException("The email verification has expired.");
        }

        if (ZonedDateTime.now(ZoneId.of(emailModel.getTimezone()))
                .isBefore(emailModel.getNextValidCodeResentDate()
                        .withZoneSameInstant(ZoneId.of(emailModel.getTimezone())))) {
            throw new EmailVerificationResendTooSoonException(
                    "Please wait before requesting a new email verification code.");
        }

        return emailModel.getCode();
    }

    public String verifyVerificationCodeModel(EmailVerificationDTO emailVerificationDTO) {

        Optional<EmailVerificationModel> matchedEmail = emailVerificationRepository
                .getEmailVerificationModelByEmail(emailVerificationDTO.email());

        EmailVerificationModel emailModel = matchedEmail
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not linked to any pending registration."));

        if (emailModel.isVerified()) {
            throw new EmailAlreadyVerifiedException("This account has already been verified.");
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
}
