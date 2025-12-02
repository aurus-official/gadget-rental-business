package com.gadget.rental.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final int PAGE_SIZE = 4;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewModel> getListRentalGadget(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by(Sort.Order.asc("id")));
        Page<ReviewModel> reviews = reviewRepository.findAll(pageable);
        return reviews.toList();
    }

    public String addReview(ReviewDTO reviewDTO) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setRate(reviewDTO.rate());
        reviewModel.setLocation(reviewDTO.location());
        reviewModel.setProfileName(reviewDTO.profileName());
        reviewModel.setReview(reviewDTO.review());
        this.reviewRepository.save(reviewModel);
        return String.format("A new review by %s has been added.", reviewDTO.profileName());
    }

    public String deleteReview(Long id) {
        this.reviewRepository.deleteById(id);

        return String.format("The review by %s has been deleted.", id);
    }
}
