package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Bill;
import com.haui.bookinghotel.domain.Room;
import com.haui.bookinghotel.domain.User;
import com.haui.bookinghotel.domain.request.BillRequest;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.domain.response.bill.BillResponse;
import com.haui.bookinghotel.repository.BillRepository;
import com.haui.bookinghotel.repository.RoomRepository;
import com.haui.bookinghotel.repository.UserRepository;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final UserService userService;
    private final RoomService roomService;

    public BillService(BillRepository billRepository, UserService userService, RoomService roomService) {
        this.billRepository = billRepository;
        this.userService = userService;
        this.roomService = roomService;
    }


    public ResultPaginationDTO handleFetchAllBills(@Filter Specification<Bill> spec, Pageable pageable) {
        Page<Bill> pageBill = this.billRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageBill.getTotalPages());
        meta.setTotal(pageBill.getTotalElements());

        res.setMeta(meta);
        res.setResult(pageBill.getContent());
        return res;
    }

    public Bill handleFetchBillById(Long id) {
        Optional<Bill> bill = this.billRepository.findById(id);
        return bill.orElse(null);
    }

    public Bill handleCreateBill(BillRequest reqBill) {
        Bill bill = new Bill();
        bill.setUsername(reqBill.getUsername());
        bill.setCheckInDate(reqBill.getCheckInDate());
        bill.setCheckOutDate(reqBill.getCheckOutDate());
        bill.setTotalCost(reqBill.getTotalCost());
        bill.setEmail(reqBill.getEmail());
        bill.setPhoneNumber(reqBill.getPhoneNumber());

        User user = this.userService.handleFetchUserById(reqBill.getUser_id());
        bill.setUser(user);
        Room room = this.roomService.handleFetchRoomById(reqBill.getRoom_id());
        bill.setRoom(room);
        return this.billRepository.save(bill);
    }

    public Bill handleUpdateBill(Bill bill) {
        Optional<Bill> oldBill = this.billRepository.findById(bill.getId());
        if (oldBill.isPresent()) {
            Bill newBill = oldBill.get();
            newBill.setUsername(bill.getUsername());
            newBill.setCheckInDate(bill.getCheckInDate());
            newBill.setCheckOutDate(bill.getCheckOutDate());
            newBill.setTotalCost(bill.getTotalCost());
            newBill.setEmail(bill.getEmail());
            newBill.setPhoneNumber(bill.getPhoneNumber());
            newBill.setRoom(bill.getRoom());
            newBill.setUser(bill.getUser());
            return billRepository.save(newBill);
        }
        return null;
    }

    public void handleDeleteBill(Long id) {
        this.billRepository.deleteById(id);
    }

    public BillResponse covertToResponse(Bill bill) {
        BillResponse res = new BillResponse();
        res.setId(bill.getId());
        res.setUsername(bill.getUsername());
        res.setCheckInDate(bill.getCheckInDate());
        res.setCheckOutDate(bill.getCheckOutDate());
        res.setTotalCost(bill.getTotalCost());
        res.setEmail(bill.getEmail());
        res.setPhoneNumber(bill.getPhoneNumber());
        res.setRoom_id(bill.getRoom().getId());
        res.setUser_id(bill.getUser().getId());
        return res;
    }
}


