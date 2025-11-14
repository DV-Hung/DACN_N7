package com.haui.bookinghotel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haui.bookinghotel.util.constant.RoomStatus;
import com.haui.bookinghotel.util.constant.RoomType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name ="rooms")
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private String roomImage;
    private int capacity;
    private int area;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String utilities;
    private double price;
    @Enumerated(EnumType.STRING)
    private RoomStatus available;

    @ManyToOne
    @JoinColumn(name ="hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Bill> bills;

}
