package com.gadget.rental.booking;

public enum BookingStatus {
    PENDING("pending"), ON_HOLD("on_hold"), PAYMENT_CONFIRMED("payment_confirmed"),
    RESTRICTED_FUNDS_CONFIRMED("restricted_funds_confirmed"), ONGOING("ongoing"), COMPLETED("completed"),
    CANCELLED("cancelled");

    String value;

    BookingStatus(String value) {
        this.value = value;
    }

}
