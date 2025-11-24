package com.haui.bookinghotel.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BillRequest {
    private String username;
    private String email;
    private String phoneNumber;
    @NotNull(message = "Check in date must not be null")
    private Instant checkInDate;
    @NotNull(message = "Check out date must not be null")
    private Instant checkOutDate;
    private double totalCost;
    @NotNull(message = "Room_Id must not be null")
    private Long room_id;
    @NotNull(message = "User_Id must not be null")
    private Long user_id;

}
