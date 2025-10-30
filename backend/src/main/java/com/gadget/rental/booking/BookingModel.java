package com.gadget.rental.booking;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    @Convert(converter = BookingProductIdsConverter.class)
    @Column(name = "product_ids")
    private List<Long> rentalGadgetProductIdList = new ArrayList<>();

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

    public void addRentalGadgetProductId(long productId) {
        rentalGadgetProductIdList.add(productId);
    }

    public List<Long> getRentalGadgetProductIdList() {
        return rentalGadgetProductIdList;
    }

    public void setValidConfirmationDateFrom(ZonedDateTime validConfirmationDateFrom) {
        this.validConfirmationDateFrom = validConfirmationDateFrom;
    }

    public void setValidConfirmationDateUntil(ZonedDateTime validConfirmationDateUntil) {
        this.validConfirmationDateUntil = validConfirmationDateUntil;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
