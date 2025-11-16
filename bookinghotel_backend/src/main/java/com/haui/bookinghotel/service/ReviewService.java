package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Review;
import com.haui.bookinghotel.domain.ReviewId;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.repository.ReviewRepository;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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
        res.setResult(pageReview.getContent());
        return res;
    }

    public Review handleFetchReviewById(ReviewId id) {
        Optional<Review> review = this.reviewRepository.findById(id);
        return review.orElse(null);
    }

    public Review handleCreateReview(Review review) {
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
}


