package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.Bill;
import com.haui.bookinghotel.domain.Room;
import com.haui.bookinghotel.domain.request.BillRequest;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.domain.response.bill.BillResponse;
import com.haui.bookinghotel.service.BillService;
import com.haui.bookinghotel.service.RoomService;
import com.haui.bookinghotel.service.UserService;
import com.haui.bookinghotel.util.annotation.ApiMessage;
import com.haui.bookinghotel.util.constant.RoomStatus;
import com.haui.bookinghotel.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BillController {
    private final BillService billService;
    private final UserService userService;
    private final RoomService roomService;

    public BillController(BillService billService, UserService userService, RoomService roomService) {
        this.billService = billService;
        this.userService = userService;
        this.roomService = roomService;
    }

    @GetMapping("/bills")
    @ApiMessage("fetch all bills")
    public ResponseEntity<ResultPaginationDTO> getAllBills(
            @Filter Specification<Bill> spec,
            Pageable pageable) {
        ResultPaginationDTO bills = this.billService.handleFetchAllBills(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(bills);
    }

    @GetMapping("/bills/{id}")
    @ApiMessage("fetch a bill by id")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Bill bill = this.billService.handleFetchBillById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bill);
    }

    @PostMapping("/bills")
    @ApiMessage("create a bill")
    public ResponseEntity<BillResponse> createNewBill(@Valid @RequestBody BillRequest reqBill) throws IdInvalidException {
        boolean isValidUser = this.userService.isIdExist(reqBill.getUser_id());
        if (!isValidUser) {
            throw new IdInvalidException("User is not exist");
        }
        boolean isValidRoom = this.roomService.isIdExist(reqBill.getRoom_id());
        if (!isValidRoom) {
            throw new IdInvalidException("Room is not exist");
        }
        Room room = this.roomService.handleFetchRoomById(reqBill.getRoom_id());
        if(room.getAvailable() != RoomStatus.AVAILABLE){
            throw new IdInvalidException("This room is booked");
        }

        Bill newBill = this.billService.handleCreateBill(reqBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.billService.covertToResponse(newBill));
    }

}


