package com.gadget.rental.booking;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<BookingModel, Long> {

    // @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE
    // bkInfo.validConfirmationDateFrom <= :dateTime AND
    // bkInfo.validConfirmationDateValid >= :dateTime")
    // Optional<List<BookingModel>> findAllValidBookings(@Param("dateTime")
    // ZonedDateTime dateTime);

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE bkInfo.validBookingDateFrom >= :currentBookingDateFrom AND bkInfo.validBookingDateUntil <= :currentBookingDateUntil")
    List<BookingModel> findAllValidBookings(
            @Param("currentBookingDateFrom") ZonedDateTime currentBookingDateFrom,
            @Param("currentBookingDateUntil") ZonedDateTime currentBookingDateUntil);

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE bkInfo.referenceNumber=?1")
    Optional<BookingModel> findBookingByReferenceNumber(String referenceNumber);
}
