package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.service.HotelService;
import com.haui.bookinghotel.util.annotation.ApiMessage;
import com.haui.bookinghotel.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<Hotel> getHotelById(@PathVariable("id") Long id) throws IdInvalidException {
        boolean isValidId = this.hotelService.isIdExist(id);
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        Hotel hotel = this.hotelService.handleFetchHotelById(id);
        return ResponseEntity.status(HttpStatus.OK).body(hotel);
    }

    @PostMapping("/hotels")
    @ApiMessage("create a hotel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> createNewHotel(@Valid @RequestBody Hotel hotel) {
        Hotel newHotel = this.hotelService.handleCreateHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
    }

    @PutMapping("/hotels")
    @ApiMessage("update hotel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(@RequestBody Hotel hotel) throws IdInvalidException {
        boolean isValidId = this.hotelService.isIdExist(hotel.getId());
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        Hotel newHotel = this.hotelService.handleUpdateHotel(hotel);
        return ResponseEntity.status(HttpStatus.OK).body(newHotel);
    }

    @DeleteMapping("/hotels/{id}")
    @ApiMessage("delete hotel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> deleteHotel(@PathVariable("id") Long id) throws IdInvalidException {
        boolean isValidId = this.hotelService.isIdExist(id);
        if (!isValidId) {
            throw new IdInvalidException("Hotel is not exist");
        }
        this.hotelService.handleDeleteHotel(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
