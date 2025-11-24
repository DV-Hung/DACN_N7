package com.haui.bookinghotel.domain.request;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.util.constant.RoomStatus;
import com.haui.bookinghotel.util.constant.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequest {
    private RoomType roomType;
    private String roomImage;
    private int capacity;
    private int area;
    private String utilities;
    @Min(value = 0, message = "Total cost must be positive")
    private double price;
    private RoomStatus available;
    @NotNull(message = "Hotel_id must not be null")
    private Long hotel_id;
}
