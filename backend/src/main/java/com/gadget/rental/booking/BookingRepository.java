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

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE " +
            "(bkInfo.validBookingDateFrom >= :currentBookingDateFrom) AND " +
            "(bkInfo.validBookingDateUntil <= :currentBookingDateUntil) AND " +
            "(bkInfo.status != 'completed') AND (bkInfo.status != 'expired') AND (bkInfo.status != 'cancelled')")
    List<BookingModel> findAllValidBookingsByDuration(
            @Param("currentBookingDateFrom") ZonedDateTime currentBookingDateFrom,
            @Param("currentBookingDateUntil") ZonedDateTime currentBookingDateUntil);

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE bkInfo.requestReferenceNumber=?1")
    Optional<BookingModel> findBookingByRequestReferenceNumber(String requestReferenceNumber);

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE bkInfo.createdBy=?1")
    List<BookingModel> findAllValidBookingsCreatedByAdmin(String createdBy);

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE bkInfo.createdFor=?1")
    List<BookingModel> findAllValidBookingsCreatedForClient(String createdFor);

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE " +
            "(bkInfo.validBookingDateFrom BETWEEN :currentBookingDateFrom AND :currentBookingDateUntil) OR " +
            "(bkInfo.validBookingDateUntil BETWEEN :currentBookingDateFrom AND :currentBookingDateUntil) AND " +
            "(bkInfo.status != 'completed') AND (bkInfo.status != 'expired') AND (bkInfo.status != 'cancelled')")
    List<BookingModel> findAllValidBookingsByMonth(
            @Param("currentBookingDateFrom") ZonedDateTime currentBookingDateFrom,
            @Param("currentBookingDateUntil") ZonedDateTime currentBookingDateUntil);

    @Query("SELECT bkInfo FROM bookingInfo bkInfo WHERE " +
            "(bkInfo.validBookingDateUntil <= NOW()) AND " +
            "(bkInfo.status != 'completed') AND (bkInfo.status != 'expired') AND (bkInfo.status != 'cancelled')")
    List<BookingModel> findAllExpiredBookings();

}
