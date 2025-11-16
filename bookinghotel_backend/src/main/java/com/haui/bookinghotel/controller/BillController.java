package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.Bill;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.service.BillService;
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
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
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
    public ResponseEntity<Bill> createNewBill(@Valid @RequestBody Bill bill) {
        Bill newBill = this.billService.handleCreateBill(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBill);
    }

    @PutMapping("/bills")
    @ApiMessage("update bill")
    public ResponseEntity<Bill> updateBill(@RequestBody Bill bill) {
        Bill newBill = this.billService.handleUpdateBill(bill);
        return ResponseEntity.status(HttpStatus.OK).body(newBill);
    }

    @DeleteMapping("/bills/{id}")
    @ApiMessage("delete bill")
    public ResponseEntity<Bill> deleteBill(@PathVariable Long id) {
        this.billService.handleDeleteBill(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}


