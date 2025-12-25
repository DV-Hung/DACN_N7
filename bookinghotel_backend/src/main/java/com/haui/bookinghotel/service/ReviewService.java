package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.domain.Review;
import com.haui.bookinghotel.domain.ReviewId;
import com.haui.bookinghotel.domain.User;
import com.haui.bookinghotel.domain.request.ReviewRequest;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.domain.response.review.ReviewResponse;
import com.haui.bookinghotel.repository.ReviewRepository;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final HotelService hotelService;

    public ReviewService(ReviewRepository reviewRepository, UserService userService, HotelService hotelService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.hotelService = hotelService;
    }

    public boolean isValidId(ReviewId id) {
        return this.reviewRepository.existsById(id);
    }

    public boolean isValidUser(Long id) {
        return this.userService.isIdExist(id);
    }

    public boolean isValidHotel(Long id) {
        return this.hotelService.isIdExist(id);
    }

    public ResultPaginationDTO handleFetchAllReviews(@Filter Specification<Review> spec, Pageable pageable) {
        Page<Review> pageReview = this.reviewRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageReview.getTotalPages());
        meta.setTotal(pageReview.getTotalElements());

        res.setMeta(meta);
        List<ReviewResponse> listUser = pageReview.getContent()
                .stream().map(item -> this.convertToResponse(item))
                .toList();
        res.setResult(listUser);
        return res;
    }

    public Review handleFetchReviewById(ReviewId id) {
        Optional<Review> review = this.reviewRepository.findById(id);
        return review.orElse(null);
    }

    public List<ReviewResponse> handleFetchReviewByHotelId(Long hotelId) {
        Hotel hotel = this.hotelService.handleFetchHotelById(hotelId);
        Set<Review> reviews = hotel.getReviews();
        return reviews.stream().map(this::convertToResponse).toList();
    }

    public Review handleCreateReview(ReviewRequest reqReview) {
        Review review = new Review();
        User user = this.userService.handleFetchUserById(reqReview.getUser_id());
        Hotel hotel = this.hotelService.handleFetchHotelById(reqReview.getHotel_id());
        Set<Review> reviewHotel = hotel.getReviews();
        hotel.setRate((hotel.getRate() * reviewHotel.size() + reqReview.getRating()) / (reviewHotel.size() + 1));
        review.setHotel(hotel);
        review.setUser(user);
        review.setRating(reqReview.getRating());
        review.setComment(reqReview.getComment());
        return this.reviewRepository.save(review);
    }

    public Review handleUpdateReview(Review review) {
        if (review.getId() != null) {
            Optional<Review> oldReview = this.reviewRepository.findById(review.getId());
            if (oldReview.isPresent()) {
                Review newReview = oldReview.get();
                newReview.setRating(review.getRating());
                newReview.setComment(review.getComment());
                newReview.setUser(review.getUser());
                newReview.setHotel(review.getHotel());
                return reviewRepository.save(newReview);
            }
        }
        return null;
    }

    public void handleDeleteReview(ReviewId id) {
        this.reviewRepository.deleteById(id);
    }

    public ReviewResponse convertToResponse(Review review) {
        ReviewResponse res = new ReviewResponse();
        res.setUser_id(review.getUser().getId());
        res.setHotel_id(review.getHotel().getId());
        res.setRating(review.getRating());
        res.setComment(review.getComment());
        return res;
    }

    public int handleFetchQuantity() {
        List<Review> reviews = this.reviewRepository.findAll();
        return reviews.size();
    }
}
