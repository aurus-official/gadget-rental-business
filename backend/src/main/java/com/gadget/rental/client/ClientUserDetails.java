package com.gadget.rental.client;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class ClientUserDetails implements UserDetails {

    private ClientModel clientModel;

    ClientUserDetails(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(
                clientModel.getClientRoleList().stream().map(role -> role.value).collect(Collectors.joining(", ")));
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
