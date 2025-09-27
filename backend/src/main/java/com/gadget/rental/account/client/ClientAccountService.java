package com.gadget.rental.account.client;

import java.util.Optional;

import com.gadget.rental.auth.verification.EmailVerificationModel;
import com.gadget.rental.auth.verification.EmailVerificationRepository;
import com.gadget.rental.auth.verification.EmailVerificationType;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailNotVerifiedException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationRoleMismatchException;
import com.gadget.rental.exception.TokenMismatchException;
import com.gadget.rental.exception.UsernameDuplicateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class ClientAccountService {

    private final ClientAccountRepository clientAccountRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ClientAccountService(ClientAccountRepository clientAccountRepository,
            EmailVerificationRepository emailVerificationRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientAccountRepository = clientAccountRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String addClientAccountAfterVerification(ClientAccountDTO clientAccountDTO, String authHeader) {

        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(clientAccountDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (!matchingEmail.isAccountTypeMatched(EmailVerificationType.CLIENT)) {
            throw new EmailVerificationRoleMismatchException(
                    "Role provided during account creation does not match the expected role.");
        }

        if (matchingEmail.isLinked()) {
            throw new EmailAlreadyBoundException("This email is linked to another account.");
        }

        if (!(matchingEmail.isVerified())) {
            throw new EmailNotVerifiedException("This email is not verified.");
        }

        if (!(matchingEmail.isAuthTokenMatched(authHeader))) {
            throw new TokenMismatchException("Token mismatch, please try registering again.");
        }

        Optional<ClientAccountModel> matchedClient = clientAccountRepository
                .findClientAccountByUsername(clientAccountDTO.username());

        if (matchedClient.isPresent()) {
            throw new UsernameDuplicateException("This username is already taken.");
        }

        emailVerificationRepository.updateEmailVerificationIsLinked(true, matchingEmail.getEmail());
        ClientAccountModel clientModel = new ClientAccountModel();
        clientModel.setUsername(clientAccountDTO.username());
        clientModel.setPassword(bCryptPasswordEncoder.encode(clientAccountDTO.password()));
        clientModel.setEmail(clientAccountDTO.email());
        clientAccountRepository.save(clientModel);

        return String.format("Client '%s' has successfully added!", clientModel.getUsername());
    }
}
