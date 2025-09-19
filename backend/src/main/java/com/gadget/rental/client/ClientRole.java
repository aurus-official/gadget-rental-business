package com.gadget.rental.client;

public enum ClientRole {
    USER("user"), ADMIN("admin");

    String value;

    ClientRole(String value) {
        this.value = value;
    }
}
