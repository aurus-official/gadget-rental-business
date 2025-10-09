package com.gadget.rental.auth.jwt;

import java.time.ZonedDateTime;
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

    @Query("SELECT rfTokenInfo FROM refreshTokenInfo rfTokenInfo WHERE rfTokenInfo.token = ?1")
    Optional<JwtRefreshTokenModel> findRefreshTokenByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE refreshTokenInfo rfTokenInfo SET rfTokenInfo.status = :status WHERE rfTokenInfo.email = :email")
    void setRefreshTokenByEmail(@Param("email") String email, @Param("status") JwtRefreshTokenStatus status);

    @Transactional
    @Modifying
    @Query("DELETE FROM refreshTokenInfo rfTokenInfo WHERE rfTokenInfo.status <> \"active\" OR rfTokenInfo.validUntil <= :datetime")
    public void deleteInvalidRefreshTokenByExpiry(@Param("datetime") ZonedDateTime datetime);

}
