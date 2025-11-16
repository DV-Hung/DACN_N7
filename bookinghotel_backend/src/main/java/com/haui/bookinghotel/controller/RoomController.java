package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.Room;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.service.RoomService;
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
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    @ApiMessage("fetch all rooms")
    public ResponseEntity<ResultPaginationDTO> getAllRooms(
            @Filter Specification<Room> spec,
            Pageable pageable) {
        ResultPaginationDTO rooms = this.roomService.handleFetchAllRooms(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }

    @GetMapping("/rooms/{id}")
    @ApiMessage("fetch a room by id")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = this.roomService.handleFetchRoomById(id);
        return ResponseEntity.status(HttpStatus.OK).body(room);
    }

    @PostMapping("/rooms")
    @ApiMessage("create a room")
    public ResponseEntity<Room> createNewRoom(@Valid @RequestBody Room room) {
        Room newRoom = this.roomService.handleCreateRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }

    @PutMapping("/rooms")
    @ApiMessage("update room")
    public ResponseEntity<Room> updateRoom(@RequestBody Room room) {
        Room newRoom = this.roomService.handleUpdateRoom(room);
        return ResponseEntity.status(HttpStatus.OK).body(newRoom);
    }

    @DeleteMapping("/rooms/{id}")
    @ApiMessage("delete room")
    public ResponseEntity<Room> deleteRoom(@PathVariable Long id) {
        this.roomService.handleDeleteRoom(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}


