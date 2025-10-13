package com.gadget.rental.auth;

import java.util.Collection;
import java.util.Collections;

import com.gadget.rental.shared.AccountType;
import com.gadget.rental.shared.BaseAccountModel;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUserDetails implements UserDetails {

    private final BaseAccountModel baseAccountModel;
    private final AccountType role;

    AuthUserDetails(BaseAccountModel baseAccountModel, AccountType role) {
        this.baseAccountModel = baseAccountModel;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.value.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return this.baseAccountModel.getPassword();
    }

    @Override
    public String getUsername() {
        return this.baseAccountModel.getEmail();
    }

}
