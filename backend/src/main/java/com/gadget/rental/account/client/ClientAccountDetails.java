package com.gadget.rental.account.client;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ClientAccountDetails implements UserDetails {

    private ClientAccountModel clientAccountModel;

    ClientAccountDetails(ClientAccountModel clientAccountModel) {
        this.clientAccountModel = clientAccountModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return clientAccountModel.getPassword();
    }

    @Override
    public String getUsername() {
        return clientAccountModel.getUsername();
    }

}
