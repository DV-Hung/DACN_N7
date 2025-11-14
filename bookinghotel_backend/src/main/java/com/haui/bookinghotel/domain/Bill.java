package com.haui.bookinghotel.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Instant checkInDate;
    private Instant checkOutDate;
    private double totalCost;
    private String email;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
