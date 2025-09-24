package com.gadget.rental.client;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ClientUserDetails implements UserDetails {

    private ClientModel clientModel;

    ClientUserDetails(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return clientModel.getPassword();
    }

    @Override
    public String getUsername() {
        return clientModel.getUsername();
    }

}
