package com.gadget.rental.rental;

public enum RentalGadgetStatus {
    AVAILABLE("available"), LEASED("leased"), PENDING("pending"), BOOKED_PAID("booked_paid"),
    BOOKED_UNPAID("booked_unpaid"), CANCELLED("cancelled"), OTHERS("others");

    String value;

    RentalGadgetStatus(String value) {
        this.value = value;
    }
}
