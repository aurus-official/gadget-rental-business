package com.gadget.rental.rental;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1")
public class RentalGadgetController {

    private final RentalGadgetService rentalGadgetService;

    @Autowired
    RentalGadgetController(RentalGadgetService rentalGadgetService) {
        this.rentalGadgetService = rentalGadgetService;
    }

    @GetMapping(path = "/gadgets")
    ResponseEntity<List<RentalGadgetModel>> handleGetRentalGadgetListingList(@RequestParam(name = "page") int page) {
        List<RentalGadgetModel> pagedRentalGadgetList = rentalGadgetService.getListRentalGadget(page);
        return ResponseEntity.ok(pagedRentalGadgetList);
    }

    @PostMapping(path = "/gadgets")
    ResponseEntity<String> createNewRentalGadgetListing(@RequestPart MultipartFile[] images,
            @RequestPart String name,
            @RequestPart String description,
            @RequestPart double price) {
        RentalGadgetDTO rentalGadgetDTO = new RentalGadgetDTO(images, name, ZonedDateTime.now(ZoneId.of("Z")), price,
                description);

        String status = rentalGadgetService.addNewRentalGadget(rentalGadgetDTO);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping(path = "/gadgets")
    ResponseEntity<String> deleteExistingRentalGadgetListing(@PathVariable("id") Long id) {
        String status = rentalGadgetService.removeExistingRentalGadget(id);
        return ResponseEntity.ok(status);
    }

    @PutMapping(path = "/gadgets")
    ResponseEntity<String> updateExistingRentalGadgetListing(@RequestBody RentalGadgetDTO rentalGadgetDTO,
            @PathVariable("id") Long id) {
        String status = rentalGadgetService.updateExistingRentalGadget(rentalGadgetDTO, id);
        return ResponseEntity.ok(status);
    }

    @PostMapping(path = "/gadgets/batch")
    ResponseEntity<List<String>> batchUpdateAndAppendNewRentalGadgetListing(@RequestPart MultipartFile excel,
            @RequestPart String rentalGadgetListingCount) {
        List<String> allProductNames = rentalGadgetService.batchUpdateAndAppendNewRentalGadgetListing(excel,
                Integer.parseInt(rentalGadgetListingCount));
        return ResponseEntity.ok().body(allProductNames);
    }

    @PostMapping(path = "/gadgets/images/{id}")
    ResponseEntity<String> uploadRentalGadgetImages(@RequestPart MultipartFile[] images,
            @PathVariable("id") Long id) {
        String productName = rentalGadgetService.uploadImagesToDirectory(images, id);
        return ResponseEntity.ok(String.format("Image/s for PRODUCT NAME \"%s\" has added.", productName));
    }

    @DeleteMapping(path = "/gadgets/images/{id}")
    ResponseEntity<String> deleteAllRentalGadgetImages(@PathVariable("id") Long id) {
        String productName = rentalGadgetService.deleteImagesFromDirectory(id);
        return ResponseEntity.ok(String.format("Image/s for PRODUCT NAME \"%s\" has added.", productName));
    }

}
