package com.gadget.rental.account.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientAccountDetailsService implements UserDetailsService {

    private final ClientAccountRepository clientAccountRepository;

    @Autowired
    ClientAccountDetailsService(ClientAccountRepository clientAccountRepository) {
        this.clientAccountRepository = clientAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ClientAccountModel> client = clientAccountRepository.findClientAccountByUsername(username);
        return new ClientAccountDetails(client.orElseThrow(() -> new UsernameNotFoundException("Username Not Found")));
    }
}
