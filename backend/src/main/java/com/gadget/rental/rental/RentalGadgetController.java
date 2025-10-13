package com.gadget.rental.rental;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class RentalGadgetController {

    private final RentalGadgetService rentalGadgetService;

    @Autowired
    RentalGadgetController(RentalGadgetService rentalGadgetService) {
        this.rentalGadgetService = rentalGadgetService;
    }

    @GetMapping(path = "/gadgets")
    ResponseEntity<List<RentalGadgetModel>> handleGetRentalGadgetModelList(@RequestParam(name = "page") int page) {
        List<RentalGadgetModel> pagedRentalGadgetList = rentalGadgetService.getListRentalGadgetModel(page);
        return ResponseEntity.ok(pagedRentalGadgetList);
    }

    @PostMapping(path = "/gadgets")
    ResponseEntity<List<RentalGadgetModel>> createNewRentalGadgetListing(@RequestParam(name = "page") int page) {
        List<RentalGadgetModel> pagedRentalGadgetList = rentalGadgetService.getListRentalGadgetModel(page);
        return ResponseEntity.ok(pagedRentalGadgetList);
    }
}
