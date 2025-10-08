package com.gadget.rental.shared;

public enum AccountType {
    CLIENT("client"), ADMIN("admin");

    public String value;

    AccountType(String value) {
        this.value = value;
    }

}
