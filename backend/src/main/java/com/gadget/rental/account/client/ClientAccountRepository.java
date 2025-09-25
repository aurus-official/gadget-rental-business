package com.gadget.rental.account.client;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAccountRepository extends CrudRepository<ClientAccountModel, Long> {

    @Query("SELECT cInfo FROM clientInfo cInfo WHERE cInfo.username=?1")
    public Optional<ClientAccountModel> findClientAccountByUsername(String username);

    @Query("SELECT cInfo FROM clientInfo cInfo WHERE cInfo.email=?1")
    public Optional<ClientAccountModel> findClientAccountByEmail(String email);
}
