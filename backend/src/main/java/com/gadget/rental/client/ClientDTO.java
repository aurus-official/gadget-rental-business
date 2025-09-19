package com.gadget.rental.client;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ClientDTO(
        @NotEmpty(message = "username is required!") @Size(min = 5, max = 15) String username,
        @NotEmpty(message = "password is required!") String password,
        String confirmPassword,
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") String email,
        @NotEmpty(message = "token is required!") String token) {

    @AssertTrue
    public boolean isMatchedPassword() {
        return password.compareTo(confirmPassword) == 0;
    }

}
