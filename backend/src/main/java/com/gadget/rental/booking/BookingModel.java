package com.gadget.rental.booking;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "bookingInfo")
@Table(name = "bookingInfo")
public class BookingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "valid_confirmation_date_from")
    private ZonedDateTime validConfirmationDateFrom;

    @Column(name = "valid_confirmation_date_until")
    private ZonedDateTime validConfirmationDateUntil;

    @Column(name = "valid_booking_date_from")
    private ZonedDateTime validBookingDateFrom;

    @Column(name = "valid_booking_date_until")
    private ZonedDateTime validBookingDateUntil;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "created_for")
    private String createdFor;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "product_ids")
    private List<Integer> rentalGadgetProductIdList = new ArrayList<>();

    public BookingModel() {
        this.referenceNumber = UUID.randomUUID().toString();
        this.validBookingDateFrom = ZonedDateTime.now(ZoneId.of("Z"));
        this.validBookingDateUntil = this.validBookingDateFrom.plusHours(12);
    }

    public Long getId() {
        return id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public ZonedDateTime getValidConfirmationDateFrom() {
        return validConfirmationDateFrom;
    }

    public ZonedDateTime getValidConfirmationDateUntil() {
        return validConfirmationDateUntil;
    }

    public String getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(String createdFor) {
        this.createdFor = createdFor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getValidBookingDateFrom() {
        return validBookingDateFrom;
    }

    public ZonedDateTime getValidBookingDateUntil() {
        return validBookingDateUntil;
    }

    public void setValidBookingDateFrom(ZonedDateTime validBookingDateFrom) {
        this.validBookingDateFrom = validBookingDateFrom;
    }

    public void setValidBookingDateUntil(ZonedDateTime validBookingDateUntil) {
        this.validBookingDateUntil = validBookingDateUntil;
    }

    public void addRentalGadgetProductId(int productId) {
        rentalGadgetProductIdList.add(productId);
    }

    public List<Integer> getRentalGadgetProductIdList() {
        return rentalGadgetProductIdList;
    }
}
