package com.haui.bookinghotel.domain.response.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {
    private Long user_id;
    private Long hotel_id;
    private double rating;
    private String comment;
}
