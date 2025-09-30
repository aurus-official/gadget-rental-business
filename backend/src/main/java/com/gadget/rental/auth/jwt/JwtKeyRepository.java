package com.gadget.rental.auth.jwt;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface JwtKeyRepository extends CrudRepository<JwtKeyModel, Long> {

    @Query("SELECT jwtInfo FROM jwtKeyInfo jwtInfo WHERE jwtInfo.isPrimary = 1")
    public JwtKeyModel findPrimaryJwtKey();
}
