package com.gadget.rental.account.client;

import com.gadget.rental.auth.verification.EmailVerificationModel;
import com.gadget.rental.auth.verification.EmailVerificationRepository;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailNotVerifiedException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationRoleMismatchException;
import com.gadget.rental.exception.TokenMismatchException;
import com.gadget.rental.shared.AccountDTO;
import com.gadget.rental.shared.AccountType;

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

    public String addClientAccountAfterVerification(AccountDTO accountDTO, String authHeader) {

        EmailVerificationModel matchingEmail = emailVerificationRepository
                .findEmailVerificationByEmail(accountDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (matchingEmail.isLinked()) {
            throw new EmailAlreadyBoundException("This email is linked to another account.");
        }

        if (!(matchingEmail.isVerified())) {
            throw new EmailNotVerifiedException("This email is not verified.");
        }

        if (!(matchingEmail.isAuthTokenMatched(authHeader))) {
            throw new TokenMismatchException("Token mismatch, please try registering again.");
        }

        if (!matchingEmail.isAccountTypeMatched(AccountType.CLIENT)) {
            throw new EmailVerificationRoleMismatchException(
                    "Role provided during account creation does not match the expected role.");
        }

        emailVerificationRepository.updateEmailVerificationIsLinked(true, matchingEmail.getEmail());
        ClientAccountModel clientModel = new ClientAccountModel();
        clientModel.setPassword(bCryptPasswordEncoder.encode(accountDTO.password()));
        clientModel.setEmail(accountDTO.email());
        clientAccountRepository.save(clientModel);

        return String.format("Client '%s' has successfully added!", clientModel.getEmail());
    }
}
