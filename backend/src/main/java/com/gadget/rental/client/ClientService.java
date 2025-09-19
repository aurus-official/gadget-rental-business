package com.gadget.rental.client;

import java.util.Optional;

import com.gadget.rental.email.EmailVerificationModel;
import com.gadget.rental.email.EmailVerificationRepository;
import com.gadget.rental.exception.ClientExistedException;

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
        Optional<ClientModel> existedClient = clientRepository
                .getClientModelByUsername(clientDTO.username().toLowerCase());

        Optional<EmailVerificationModel> matchingVerificationModel = emailVerificationRepository
                .getEmailVerificationModelByEmail("");

        if (existedClient.isPresent()) {
            throw new ClientExistedException("username has already existed!");
        }
        ClientModel clientModel = new ClientModel();
        clientModel.setUsername(clientDTO.username());
        clientModel.setPassword(clientDTO.password());
        clientModel.addRole(ClientRole.USER);
        System.out.println(ClientRole.USER.value);
        clientRepository.save(clientModel);

        return String.format("client '%s' successfully added!", clientModel.getUsername());
    }
}
