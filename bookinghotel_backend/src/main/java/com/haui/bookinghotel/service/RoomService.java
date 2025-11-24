package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.domain.Room;
import com.haui.bookinghotel.domain.request.RoomRequest;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.repository.RoomRepository;
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

    public boolean isIdExist(Long id){
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
        room.setArea(reqRoom.getArea());
        room.setUtilities(reqRoom.getUtilities());
        room.setPrice(reqRoom.getPrice());
        room.setAvailable(reqRoom.getAvailable());
        room.setHotel(hotel);
        return this.roomRepository.save(room);
    }

    public Room handleUpdateRoom(Room room) {
        Optional<Room> oldRoom = this.roomRepository.findById(room.getId());
        if (oldRoom.isPresent()) {
            Room newRoom = oldRoom.get();
            newRoom.setRoomType(room.getRoomType());
            newRoom.setRoomImage(room.getRoomImage());
            newRoom.setCapacity(room.getCapacity());
            newRoom.setArea(room.getArea());
            newRoom.setUtilities(room.getUtilities());
            newRoom.setPrice(room.getPrice());
            newRoom.setAvailable(room.getAvailable());
            newRoom.setHotel(room.getHotel());
            return roomRepository.save(newRoom);
        }
        return null;
    }

    public void handleDeleteRoom(Long id) {
        this.roomRepository.deleteById(id);
    }
}


