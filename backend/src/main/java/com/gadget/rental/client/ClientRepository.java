package com.gadget.rental.client;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<ClientModel, Long> {

    @Query("SELECT cInfo FROM clientInfo cInfo WHERE cInfo.username=?1")
    public Optional<ClientModel> getClientModelByUsername(String username);

    @Query("SELECT cInfo FROM clientInfo cInfo WHERE cInfo.email=?1")
    public Optional<ClientModel> getClientModelByEmail(String email);
}
