package com.haui.bookinghotel.repository;

import com.haui.bookinghotel.domain.Review;
import com.haui.bookinghotel.domain.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewId>, JpaSpecificationExecutor<Review> {
}


