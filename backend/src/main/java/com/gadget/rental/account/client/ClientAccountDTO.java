package com.gadget.rental.account.client;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ClientAccountDTO(
        @NotEmpty(message = "Username is required!") @Size(min = 5, max = 15) String username,
        @NotEmpty(message = "Password is required.") String password,
        @NotEmpty(message = "Confirm password is required.") String confirmPassword,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") String email,
        @NotEmpty(message = "Token is required!") String token) {

    @AssertTrue
    public boolean isMatchedPassword() {
        return password.compareTo(confirmPassword) == 0;
    }

}
