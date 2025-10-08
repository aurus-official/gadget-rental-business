package com.gadget.rental.auth.jwt;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRefreshTokenRepository extends CrudRepository<JwtRefreshTokenModel, Long> {

    @Query("SELECT rfTokenInfo FROM refreshTokenInfo rfTokenInfo WHERE rfTokenInfo.email = ?1")
    Optional<JwtRefreshTokenModel> findRefreshTokenByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE refreshTokenInfo rfTokenInfo SET rfTokenInfo.status = :status WHERE rfTokenInfo.email = :email")
    void setRefreshTokenByEmail(@Param("email") String email, @Param("status") JwtRefreshTokenStatus status);

}
