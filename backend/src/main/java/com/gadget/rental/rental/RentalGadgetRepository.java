package com.gadget.rental.rental;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalGadgetRepository
        extends ListCrudRepository<RentalGadgetModel, Long>, ListPagingAndSortingRepository<RentalGadgetModel, Long> {
    @Query("SELECT rentalInfo FROM rentalGadgetInfo rentalInfo")
    List<RentalGadgetModel> findRentalGadgetByRangeOfId(Pageable pageable);

    @Query("SELECT rentalInfo FROM rentalGadgetInfo rentalInfo")
    List<RentalGadgetModel> findIdByRentalGadget();

    @Query("SELECT rentalInfo FROM rentalGadgetInfo rentalInfo WHERE rentalInfo.name = ?1")
    Optional<RentalGadgetModel> findRentalGadgetByName(String name);
}
