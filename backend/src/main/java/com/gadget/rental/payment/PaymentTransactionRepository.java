package com.gadget.rental.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends CrudRepository<PaymentTransactionModel, Long> {
    @Query("SELECT pTInfo FROM paymentTransactionInfo pTInfo WHERE pTInfo.requestReferenceNumber=?1")
    Optional<PaymentTransactionModel> findPaymentTransactionByRequestReferenceNumber(String requestReferenceNumber);

    // @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE " +
    // "(bkInfo.validBookingDateFrom BETWEEN :currentBookingDateFrom AND
    // :currentBookingDateUntil) OR " +
    // "(bkInfo.validBookingDateUntil BETWEEN :currentBookingDateFrom AND
    // :currentBookingDateUntil)")
    // List<BookingModel> findAllValidBookingsByMonth(
    // @Param("currentBookingDateFrom") ZonedDateTime currentBookingDateFrom,
    // @Param("currentBookingDateUntil") ZonedDateTime currentBookingDateUntil);
}
