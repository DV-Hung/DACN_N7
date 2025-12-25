package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.repository.HotelRepository;
import com.haui.bookinghotel.util.constant.RoomStatus;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public int handleFetchQuantity() {
        return (int) this.hotelRepository.count();
    }

    public boolean isIdExist(Long id) {
        return hotelRepository.existsById(id);
    }

    public ResultPaginationDTO handleFetchAllHotels(@Filter Specification<Hotel> spec, Pageable pageable) {
        Specification<Hotel> isActiveSpec = (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("is_active"), true);

        Specification<Hotel> finalSpec = isActiveSpec;
        if (spec != null) {
            finalSpec = spec.and(isActiveSpec);
        }
        Page<Hotel> pageHotel = this.hotelRepository.findAll(finalSpec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageHotel.getTotalPages());
        meta.setTotal(pageHotel.getTotalElements());

        res.setMeta(meta);
        res.setResult(pageHotel.getContent());
        return res;
    }

    public Hotel handleFetchHotelById(Long id) {
        Optional<Hotel> hotel = this.hotelRepository.findById(id);
        return hotel.orElse(null);
    }

    public Hotel handleCreateHotel(Hotel hotel) {
        hotel.set_active(true);
        hotel.setRate(9);
        return this.hotelRepository.save(hotel);
    }

    public Hotel handleUpdateHotel(Hotel hotel) {
        Optional<Hotel> oldHotel = this.hotelRepository.findById(hotel.getId());
        if (oldHotel.isPresent()) {
            Hotel newHotel = oldHotel.get();
            newHotel.setName(hotel.getName());
            newHotel.setImage(hotel.getImage());
            newHotel.setAddress(hotel.getAddress());
            newHotel.setIntroduction(hotel.getIntroduction());
            newHotel.setUtilities(hotel.getUtilities());
            return hotelRepository.save(newHotel);
        }
        return null;
    }

    public void handleDeleteHotel(Long id) {
        Hotel hotel = this.handleFetchHotelById(id);
        boolean isRoomAvailable = hotel.getRooms().stream()
                .anyMatch(room -> room.getAvailable() != RoomStatus.AVAILABLE);
        if (isRoomAvailable) {
            throw new IllegalStateException("Không thể xóa khách sạn có phòng đang được đặt");
        }
        hotel.set_active(false);
        this.hotelRepository.save(hotel);
    }
}
