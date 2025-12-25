package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.domain.Room;
import com.haui.bookinghotel.domain.request.RoomRequest;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.repository.RoomRepository;
import com.haui.bookinghotel.util.constant.RoomStatus;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public boolean isIdExist(Long id) {
        return roomRepository.existsById(id);
    }

    public ResultPaginationDTO handleFetchAllRooms(@Filter Specification<Room> spec, Pageable pageable) {
        Page<Room> pageRoom = this.roomRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageRoom.getTotalPages());
        meta.setTotal(pageRoom.getTotalElements());

        res.setMeta(meta);
        res.setResult(pageRoom.getContent());
        return res;
    }

    public Room handleFetchRoomById(Long id) {
        Optional<Room> room = this.roomRepository.findById(id);
        return room.orElse(null);
    }

    public Room handleCreateRoom(RoomRequest reqRoom, Hotel hotel) {
        Room room = new Room();
        room.setRoomType(reqRoom.getRoomType());
        room.setRoomImage(reqRoom.getRoomImage());
        room.setCapacity(reqRoom.getCapacity());
        room.setPrice(reqRoom.getPrice());
        room.setAvailable(reqRoom.getAvailable());
        room.setHotel(hotel);
        room.set_active(true);
        return this.roomRepository.save(room);
    }

    public Room handleUpdateRoom(RoomRequest room, Hotel hotel) {
        Optional<Room> oldRoom = this.roomRepository.findById(room.getId());
        if (oldRoom.isPresent()) {
            Room newRoom = oldRoom.get();
            newRoom.setRoomType(room.getRoomType());
            newRoom.setRoomImage(room.getRoomImage());
            newRoom.setCapacity(room.getCapacity());

            newRoom.setPrice(room.getPrice());
            newRoom.setAvailable(room.getAvailable());

            newRoom.setHotel(hotel);
            return roomRepository.save(newRoom);
        }
        return null;
    }

    public boolean checkRoomAvailable(Long id) {
        Optional<Room> room = this.roomRepository.findById(id);
        if (room.isPresent()) {
            return room.get().getAvailable() == RoomStatus.AVAILABLE;
        }
        return false;
    }

    public void handleDeleteRoom(Long id) {
        Room room = this.handleFetchRoomById(id);
        if (this.checkRoomAvailable(id)) {
            room.set_active(false);
            this.roomRepository.save(room);
        } else {
            throw new IllegalStateException("Không thể xóa phòng đã được đặt");
        }
    }

    public int handleFetchQuantity() {
        return (int) this.roomRepository.count();
    }

    public ResultPaginationDTO handleFetchRoomsByHotelId(Long hotelId, Specification<Room> spec, Pageable pageable) {
        Specification<Room> hotelSpec = (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("hotel").get("id"), hotelId);

        Specification<Room> activeCondition = (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("is_active"), true);

        Specification<Room> finalSpec = hotelSpec.and(activeCondition);
        if (spec != null) {
            finalSpec = finalSpec.and(spec);
        }
        Page<Room> pageRoom = this.roomRepository.findAll(finalSpec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageRoom.getTotalPages());
        meta.setTotal(pageRoom.getTotalElements());

        res.setMeta(meta);
        res.setResult(pageRoom.getContent());
        return res;
    }

    public RoomRequest convertToRequest(Room room) {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setId(room.getId());
        roomRequest.setRoomType(room.getRoomType());
        roomRequest.setRoomImage(room.getRoomImage());
        roomRequest.setCapacity(room.getCapacity());

        roomRequest.setPrice(room.getPrice());
        roomRequest.setAvailable(room.getAvailable());
        roomRequest.setHotel_id(room.getHotel().getId());
        return roomRequest;
    }
}
