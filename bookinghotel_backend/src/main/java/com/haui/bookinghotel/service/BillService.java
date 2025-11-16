package com.haui.bookinghotel.service;

import com.haui.bookinghotel.domain.Bill;
import com.haui.bookinghotel.domain.response.Meta;
import com.haui.bookinghotel.domain.response.ResultPaginationDTO;
import com.haui.bookinghotel.repository.BillRepository;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BillService {
    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
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

    public Bill handleCreateBill(Bill bill) {
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
}


