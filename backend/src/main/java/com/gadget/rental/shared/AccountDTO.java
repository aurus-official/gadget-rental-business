package com.gadget.rental.shared;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AccountDTO(
        @Size(min = 4, max = 12, message = "Password length is invalid. (4 - 12)") @NotEmpty(message = "Password is required.") String password,
        @NotEmpty(message = "Confirm password is required.") String confirmPassword,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String email) {

    @AssertTrue(message = "Passwords should match.")
    public boolean isMatchedPassword() {
        return password.compareTo(confirmPassword) == 0;
    }

}
