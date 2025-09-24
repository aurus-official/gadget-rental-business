package com.gadget.rental.admin;

import java.util.Optional;

import com.gadget.rental.client.ClientDTO;
import com.gadget.rental.client.ClientModel;
import com.gadget.rental.client.ClientRepository;
import com.gadget.rental.email.EmailVerificationModel;
import com.gadget.rental.email.EmailVerificationRepository;
import com.gadget.rental.exception.ClientUsernameDuplicateException;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailNotVerifiedException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.TokenMismatchException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final ClientRepository clientRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AdminService(ClientRepository clientRepository, EmailVerificationRepository emailVerificationRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String addNewClientAfterVerification(ClientDTO clientDTO) {

        EmailVerificationModel matchedEmailVerificationModel = emailVerificationRepository
                .findEmailVerificationModelByEmail(clientDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (matchedEmailVerificationModel.isLinked()) {
            throw new EmailAlreadyBoundException("This email is linked to another account.");
        }

        if (!(matchedEmailVerificationModel.isVerified())) {
            throw new EmailNotVerifiedException("This email is not verified.");
        }

        if (clientDTO.token().compareTo(matchedEmailVerificationModel.getTokenAccountCreation()) != 0) {
            throw new TokenMismatchException("Token mismatch, please try registering again.");
        }

        Optional<ClientModel> matchedClient = clientRepository
                .findClientModelByUsername(clientDTO.username());

        if (matchedClient.isPresent()) {
            throw new ClientUsernameDuplicateException("This username is already taken.");
        }

        emailVerificationRepository.updateEmailVerificationIsLinked(true, matchedEmailVerificationModel.getEmail());
        ClientModel clientModel = new ClientModel();
        clientModel.setUsername(clientDTO.username());
        clientModel.setPassword(bCryptPasswordEncoder.encode(clientDTO.password()));
        clientModel.setEmail(clientDTO.email());
        clientRepository.save(clientModel);

        return String.format("Client '%s' has successfully added!", clientModel.getUsername());
    }
}
