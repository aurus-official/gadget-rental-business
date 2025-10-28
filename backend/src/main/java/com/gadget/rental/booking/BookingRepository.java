package com.gadget.rental.booking;

import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<BookingModel, Long> {

    // @Transactional
    // @Modifying
    // @Query("DELETE FROM refreshTokenInfo rfTokenInfo WHERE rfTokenInfo.status <>
    // \"active\" OR rfTokenInfo.validUntil <= :datetime")
    // public void deleteInvalidRefreshTokenByExpiry(@Param("datetime")
    // ZonedDateTime datetime);

}
