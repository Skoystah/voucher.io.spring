package org.gdo.voucherio.voucher.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.gdo.voucherio.voucher.service.VoucherService;
import org.gdo.voucherio.voucher.model.VoucherRequest;
import org.gdo.voucherio.voucher.model.VoucherResponse;
import org.gdo.voucherio.voucher.service.VoucherSearchCriteria;

@RestController
@RequestMapping("/vouchers")
@Slf4j
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping
    public List<VoucherResponse> findAll(
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) Boolean includeUsed) {

        return this.voucherService.findAll(new VoucherSearchCriteria(duration, includeUsed));
    }

    @GetMapping("/{code}")
    public VoucherResponse findByCode(@PathVariable String code) {
        return this.voucherService.findByCode(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherResponse addVoucher(@RequestBody VoucherRequest voucherRequest) {
        return this.voucherService.addVoucher(voucherRequest);
    }

    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code) {
        this.voucherService.delete(code);
    }

    @PutMapping("/{code}")
    public void useVoucher(@PathVariable String code) {
        this.voucherService.useVoucher(code);
    }

}
