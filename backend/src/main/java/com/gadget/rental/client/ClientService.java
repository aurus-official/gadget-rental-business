package com.gadget.rental.client;

import java.util.Optional;

import com.gadget.rental.email.EmailVerificationModel;
import com.gadget.rental.email.EmailVerificationRepository;
import com.gadget.rental.exception.ClientExistedException;
import com.gadget.rental.exception.EmailNotVerifiedException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.TokenMismatchException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ClientService {

    private final ClientRepository clientRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    ClientService(ClientRepository clientRepository, EmailVerificationRepository emailVerificationRepository) {
        this.clientRepository = clientRepository;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    public String addNewClientAfterVerification(ClientDTO clientDTO) {
        Optional<ClientModel> matchedClient = clientRepository
                .findClientModelByUsername(clientDTO.username());

        if (matchedClient.isPresent()) {
            throw new ClientExistedException("This username is already taken.");
        }

        EmailVerificationModel matchedEmailVerificationModel = emailVerificationRepository
                .findEmailVerificationModelByEmail(clientDTO.email())
                .orElseThrow(() -> new EmailVerificationRequestNotExistedException(
                        "This email is not associated to any verification."));

        if (!(matchedEmailVerificationModel.isVerified())) {
            throw new EmailNotVerifiedException("This email is not verified.");
        }

        if (clientDTO.token().compareTo(matchedEmailVerificationModel.getTokenAccountCreation()) != 0) {
            throw new TokenMismatchException("Token mismatch, please try registering again.");
        }

        ClientModel clientModel = new ClientModel();
        clientModel.setUsername(clientDTO.username());
        clientModel.setPassword(clientDTO.password());
        clientModel.setEmail(clientDTO.email());
        clientModel.addRole(ClientRole.USER);
        System.out.println(ClientRole.USER.value);
        clientRepository.save(clientModel);

        return String.format("Client '%s' has successfully added!", clientModel.getUsername());
    }
}
