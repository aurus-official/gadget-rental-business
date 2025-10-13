package com.gadget.rental.rental;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RentalGadgetService {

    private final RentalGadgetRepository rentalGadgetRepository;
    private final int PAGE_SIZE = 16;

    @Autowired
    RentalGadgetService(RentalGadgetRepository rentalGadgetRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    public List<RentalGadgetModel> getListRentalGadgetModel(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by(Sort.Order.asc("id")));
        Page<RentalGadgetModel> allRentalGadgetModel = rentalGadgetRepository.findAll(pageable);
        return allRentalGadgetModel.toList();
    }
}
