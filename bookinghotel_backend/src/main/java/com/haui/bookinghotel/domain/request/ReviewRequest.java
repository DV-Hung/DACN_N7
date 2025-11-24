package com.haui.bookinghotel.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @NotNull(message = "User_id must not be null")
    private Long user_id;
    @NotNull(message = "Hotel_id must not be null")
    private Long hotel_id;
    private double rating;
    private String comment;
}
