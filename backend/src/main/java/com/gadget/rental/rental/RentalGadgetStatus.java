package com.gadget.rental.rental;

public enum RentalGadgetStatus {
    AVAILABLE("available"), NOT_AVAILABLE("not_available");

    String value;

    RentalGadgetStatus(String value) {
        this.value = value;
    }
}
