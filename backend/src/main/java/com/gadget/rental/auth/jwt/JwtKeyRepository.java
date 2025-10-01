package com.gadget.rental.auth.jwt;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface JwtKeyRepository extends CrudRepository<JwtKeyModel, Long> {

    @Query("SELECT jwtInfo FROM jwtKeyInfo jwtInfo WHERE jwtInfo.validFrom < ?1")
    public JwtKeyModel findNextPrimaryJwtKey(ZonedDateTime nextValidUntilPTime);

}
