package com.gadget.rental.account.verification;

import java.time.ZonedDateTime;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends CrudRepository<EmailVerificationModel, Long> {
    @Query("SELECT vrInfo FROM emailVerificationInfo vrInfo WHERE vrInfo.email=?1")
    public Optional<EmailVerificationModel> findEmailVerificationByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM emailVerificationInfo vrInfo WHERE vrInfo.expiry <= :datetime")
    public void deleteExpiredEmailVerification(@Param("datetime") ZonedDateTime datetime);

    @Transactional
    @Modifying
    @Query("UPDATE emailVerificationInfo vrInfo SET vrInfo.tokenAccountCreation = :token WHERE vrInfo.email = :email")
    public void updateEmailVerificationToken(@Param("token") String token, @Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE emailVerificationInfo vrInfo SET vrInfo.isVerified = :isVerified WHERE vrInfo.email = :email")
    public void updateEmailVerificationIsVerified(@Param("isVerified") boolean isVerified,
            @Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE emailVerificationInfo vrInfo SET vrInfo.isLinked = :isLinked WHERE vrInfo.email = :email")
    public void updateEmailVerificationIsLinked(@Param("isLinked") boolean isLinked,
            @Param("email") String email);
}
