package com.gadget.rental.auth;

public enum AuthRoleType {
    CLIENT("client"), ADMIN("admin");

    String value;

    AuthRoleType(String value) {
        this.value = value;
    }

}
