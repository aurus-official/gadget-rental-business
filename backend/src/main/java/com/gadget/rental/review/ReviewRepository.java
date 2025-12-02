package com.gadget.rental.review;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository
        extends ListCrudRepository<ReviewModel, Long>, ListPagingAndSortingRepository<ReviewModel, Long> {
}
