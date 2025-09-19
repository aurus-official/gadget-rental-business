package com.gadget.rental.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Autowired
    ClientUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ClientModel> client = clientRepository.getClientModelByUsername(username);
        return new ClientUserDetails(client.orElseThrow(() -> new UsernameNotFoundException("Username Not Found")));
    }
}
