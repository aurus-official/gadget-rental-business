package com.gadget.rental.review;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class ReviewController {

    private ReviewService reviewService;

    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    ResponseEntity<String> addReview(@RequestBody ReviewDTO reviewDTO) {
        String status = reviewService.addReview(reviewDTO);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/reviews/{id}")
    ResponseEntity<String> addReview(@PathVariable Long id) {
        String status = reviewService.deleteReview(id);
        return ResponseEntity.ok(status);
    }

    @GetMapping(path = "/reviews")
    ResponseEntity<List<ReviewModel>> handleGetRentalGadgetListingList(@RequestParam(name = "page") int page) {
        List<ReviewModel> pagedRentalGadgetList = reviewService.getListRentalGadget(page);
        return ResponseEntity.ok(pagedRentalGadgetList);
    }

}
