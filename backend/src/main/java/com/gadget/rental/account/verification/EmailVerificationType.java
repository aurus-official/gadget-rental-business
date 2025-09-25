package com.gadget.rental.account.verification;

public enum EmailVerificationType {
    CLIENT("client"), ADMIN("admin");

    String value;

    EmailVerificationType(String value) {
        this.value = value;
    }

}
