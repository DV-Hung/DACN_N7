package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.service.HotelService;
import com.haui.bookinghotel.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    @ApiMessage("fetch all hotels")
    public ResponseEntity<ResultPaginationDTO> getAllHotels(
            @Filter Specification<Hotel> spec,
            Pageable pageable) {
        ResultPaginationDTO hotels = this.hotelService.handleFetchAllHotels(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(hotels);
    }

    @GetMapping("/hotels/{id}")
    @ApiMessage("fetch a hotel by id")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Hotel hotel = this.hotelService.handleFetchHotelById(id);
        return ResponseEntity.status(HttpStatus.OK).body(hotel);
    }

    @PostMapping("/hotels")
    @ApiMessage("create a hotel")
    public ResponseEntity<Hotel> createNewHotel(@Valid @RequestBody Hotel hotel) {
        Hotel newHotel = this.hotelService.handleCreateHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
    }

    @PutMapping("/hotels")
    @ApiMessage("update hotel")
    public ResponseEntity<Hotel> updateHotel(@RequestBody Hotel hotel) {
        Hotel newHotel = this.hotelService.handleUpdateHotel(hotel);
        return ResponseEntity.status(HttpStatus.OK).body(newHotel);
    }

    @DeleteMapping("/hotels/{id}")
    @ApiMessage("delete hotel")
    public ResponseEntity<Hotel> deleteHotel(@PathVariable Long id) {
        this.hotelService.handleDeleteHotel(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}


