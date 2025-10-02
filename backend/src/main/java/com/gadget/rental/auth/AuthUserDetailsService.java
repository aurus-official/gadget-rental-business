package com.gadget.rental.auth;

import java.util.Optional;

import com.gadget.rental.account.admin.AdminAccountModel;
import com.gadget.rental.account.admin.AdminAccountRepository;
import com.gadget.rental.account.client.ClientAccountModel;
import com.gadget.rental.account.client.ClientAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final AdminAccountRepository adminAccountRepository;
    private final ClientAccountRepository clientAccountRepository;

    @Autowired
    public AuthUserDetailsService(AdminAccountRepository adminAccountRepository,
            ClientAccountRepository clientAccountRepository) {
        this.adminAccountRepository = adminAccountRepository;
        this.clientAccountRepository = clientAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AdminAccountModel> matchingAdminAccount = adminAccountRepository.findAdminAccountByEmail(email);

        if (matchingAdminAccount.isPresent()) {
            AdminAccountModel adminAccountModel = matchingAdminAccount.get();
            return new AuthUserDetails(adminAccountModel, AuthRoleType.ADMIN);
        }

        Optional<ClientAccountModel> matchingClientAccount = clientAccountRepository.findClientAccountByEmail(email);

        if (matchingClientAccount.isPresent()) {
            ClientAccountModel clientAccountModel = matchingClientAccount.get();
            return new AuthUserDetails(clientAccountModel, AuthRoleType.ADMIN);
        }

        throw new UsernameNotFoundException("Account not found.");
    }
}
