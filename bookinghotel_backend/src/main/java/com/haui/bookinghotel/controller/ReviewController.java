package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.Review;
import com.haui.bookinghotel.domain.ReviewId;
import com.haui.bookinghotel.domain.request.ReviewRequest;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.domain.response.review.ReviewResponse;
import com.haui.bookinghotel.service.ReviewService;
import com.haui.bookinghotel.util.annotation.ApiMessage;
import com.haui.bookinghotel.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    @ApiMessage("fetch all reviews")
    public ResponseEntity<ResultPaginationDTO> getAllReviews(
            @Filter Specification<Review> spec,
            Pageable pageable) {
        ResultPaginationDTO reviews = this.reviewService.handleFetchAllReviews(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/reviews/{userId}/{hotelId}")
    @ApiMessage("fetch a review by id")
    public ResponseEntity<ReviewResponse> getReviewById(
            @PathVariable Long userId,
            @PathVariable Long hotelId) throws IdInvalidException {
        ReviewId reviewId = new ReviewId(userId, hotelId);
        boolean isValidId = this.reviewService.isValidId(reviewId);
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        Review review = this.reviewService.handleFetchReviewById(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(this.reviewService.convertToResponse(review));
    }

    @GetMapping("/reviews/hotels/{hotelId}")
    @ApiMessage("fetch a review by hotel id")
    public ResponseEntity<List<ReviewResponse>> getReviewByHotelId(
            @PathVariable("hotelId") Long hotelId) throws IdInvalidException {
        boolean isValidId = this.reviewService.isValidHotel(hotelId);
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        List<ReviewResponse> reviews = this.reviewService.handleFetchReviewByHotelId(hotelId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/reviews/hotels/count/{hotelId}")
    @ApiMessage("fetch quantity review by hotel id")
    public ResponseEntity<Integer> fetchQuantityByHotel(@PathVariable("hotelId") Long hotelId)
            throws IdInvalidException {
        boolean isValidId = this.reviewService.isValidHotel(hotelId);
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        List<ReviewResponse> reviews = this.reviewService.handleFetchReviewByHotelId(hotelId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews.size());
    }

    @PostMapping("/reviews")
    @ApiMessage("create a review")
    public ResponseEntity<ReviewResponse> createNewReview(@Valid @RequestBody ReviewRequest reqReview)
            throws IdInvalidException {
        boolean isValidUser = this.reviewService.isValidUser(reqReview.getUser_id());
        if (!isValidUser) {
            throw new IdInvalidException("User is not exist");
        }
        boolean isValidHotel = this.reviewService.isValidHotel(reqReview.getHotel_id());
        if (!isValidHotel) {
            throw new IdInvalidException("Hotel is not exist");
        }
        ReviewId reviewId = new ReviewId(reqReview.getUser_id(), reqReview.getHotel_id());
        boolean isValidReview = this.reviewService.isValidId(reviewId);
        if (isValidReview) {
            throw new IdInvalidException("Bạn đã đánh giá khách sạn này");
        }
        Review newReview = this.reviewService.handleCreateReview(reqReview);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.reviewService.convertToResponse(newReview));
    }

    @PutMapping("/reviews")
    @ApiMessage("update review")
    public ResponseEntity<Review> updateReview(@RequestBody Review review) throws IdInvalidException {
        boolean isValidId = this.reviewService.isValidId(review.getId());
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        Review newReview = this.reviewService.handleUpdateReview(review);
        return ResponseEntity.status(HttpStatus.OK).body(newReview);
    }

    @DeleteMapping("/reviews/{userId}/{hotelId}")
    @ApiMessage("delete review")
    public ResponseEntity<Review> deleteReview(
            @PathVariable Long userId,
            @PathVariable Long hotelId) throws IdInvalidException {
        ReviewId reviewId = new ReviewId(userId, hotelId);
        boolean isValidId = this.reviewService.isValidId(reviewId);
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        this.reviewService.handleDeleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }

    @GetMapping("/reviews/count")
    public ResponseEntity<Integer> fetchQuantity() {
        int quantity = this.reviewService.handleFetchQuantity();
        return ResponseEntity.status(HttpStatus.OK).body(quantity);
    }

}
