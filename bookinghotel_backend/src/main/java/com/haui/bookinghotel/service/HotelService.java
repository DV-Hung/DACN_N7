package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Hotel;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.repository.HotelRepository;
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

    public boolean isIdExist(Long id){
        return hotelRepository.existsById(id);
    }

    public ResultPaginationDTO handleFetchAllHotels(@Filter Specification<Hotel> spec, Pageable pageable) {
        Page<Hotel> pageHotel = this.hotelRepository.findAll(spec, pageable);
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
        return this.hotelRepository.save(hotel);
    }

    public Hotel handleUpdateHotel(Hotel hotel) {
        Optional<Hotel> oldHotel = this.hotelRepository.findById(hotel.getId());
        if (oldHotel.isPresent()) {
            Hotel newHotel = oldHotel.get();
            newHotel.setName(hotel.getName());
            newHotel.setImage(hotel.getImage());
            newHotel.setRate(hotel.getRate());
            newHotel.setAddress(hotel.getAddress());
            newHotel.setIntroduction(hotel.getIntroduction());
            newHotel.setUtilities(hotel.getUtilities());
            return hotelRepository.save(newHotel);
        }
        return null;
    }

    public void handleDeleteHotel(Long id) {
        this.hotelRepository.deleteById(id);
    }
}


