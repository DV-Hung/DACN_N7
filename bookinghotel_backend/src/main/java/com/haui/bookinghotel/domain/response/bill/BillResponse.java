package com.haui.bookinghotel.domain.response.bill;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BillResponse {
    private Long id;
    private String username;
    private Instant checkInDate;
    private Instant checkOutDate;
    private double totalCost;
    private String email;
    private String phoneNumber;
    private Long user_id;
    private Long room_id;
}
