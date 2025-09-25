package com.gadget.rental.account.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminAccountRepository extends CrudRepository<AdminAccountModel, Long> {

    @Query("SELECT aInfo FROM adminInfo aInfo WHERE aInfo.username=?1")
    public Optional<AdminAccountModel> findAdminAccountByUsername(String username);

    @Query("SELECT aInfo FROM adminInfo aInfo WHERE aInfo.email=?1")
    public Optional<AdminAccountModel> findAdminAccountByEmail(String email);
}
