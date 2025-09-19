package com.gadget.rental.email;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.gadget.rental.client.ClientModel;
import com.gadget.rental.client.ClientRepository;
import com.gadget.rental.exception.EmailAlreadyBindedException;
import com.gadget.rental.exception.EmailVerificationFailedException;

import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final ClientRepository clientRepository;

    EmailVerificationService(EmailVerificationRepository emailVerificationRepository,
            ClientRepository clientRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.clientRepository = clientRepository;
    }

    public String createVerificationCodeModel(EmailDTO emailDTO) {
        Optional<ClientModel> bindedClientModelToEmail = clientRepository.getClientModelByEmail(emailDTO.email());

        if (bindedClientModelToEmail.isPresent()) {
            throw new EmailAlreadyBindedException("This email is already linked to another account.");
        }

        Optional<EmailVerificationModel> matchedEmail = emailVerificationRepository
                .getEmailVerificationModelByEmail(emailDTO.email());

        if (matchedEmail.isPresent()) {
            return matchedEmail.get().getCode();
        }

        String verificationCode = EmailCodeGenerator.generateVerificationCode();
        EmailVerificationModel emailVerificationModel = new EmailVerificationModel();
        emailVerificationModel.setEmail(emailDTO.email());
        emailVerificationModel.setCode(verificationCode);
        emailVerificationModel.setExpiry(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(3l));
        emailVerificationModel.setTimezone(emailDTO.timezone());
        emailVerificationRepository.save(emailVerificationModel);
        return verificationCode;
    }

    public String verifyVerificationCodeModel(EmailVerificationDTO emailVerificationDTO) {
        Optional<EmailVerificationModel> matchedEmail = emailVerificationRepository
                .getEmailVerificationModelByEmail(emailVerificationDTO.email());

        EmailVerificationModel emailModel = matchedEmail
                .orElseThrow(() -> new EmailVerificationFailedException("Email verification has failed!"));

        if (emailModel.getEmail().compareTo(emailVerificationDTO.email()) != 0) {
            throw new EmailVerificationFailedException("Email verification has failed!");
        }

        if (emailModel.getCode().compareTo(emailVerificationDTO.code()) != 0) {
            throw new EmailVerificationFailedException("Email verification has failed!");
        }

        if (!(ZonedDateTime.now(ZoneId.of(emailModel.getTimezone()))
                .isBefore(matchedEmail.get().getExpiry().withZoneSameInstant(ZoneId.of(emailModel.getTimezone()))))) {
            throw new EmailVerificationFailedException("Email verification has expired!");
        }

        if (emailModel.isAccountCreated()) {
            throw new EmailVerificationFailedException("");
        }

        String token = UUID.randomUUID().toString();
        emailVerificationRepository.updateEmailVerificationToken(token, emailModel.getEmail());

        return token;
    }
}
