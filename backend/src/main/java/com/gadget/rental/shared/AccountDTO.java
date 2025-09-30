package com.gadget.rental.shared;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AccountDTO(
        @NotEmpty(message = "Password is required.") String password,
        @NotEmpty(message = "Confirm password is required.") String confirmPassword,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") String email) {

    @AssertTrue
    public boolean isMatchedPassword() {
        return password.compareTo(confirmPassword) == 0;
    }

}
