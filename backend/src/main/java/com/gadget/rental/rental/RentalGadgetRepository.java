package com.gadget.rental.rental;

import java.util.List;

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
}
