package org.gdo.voucherio.voucher;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.gdo.voucherio.voucher.exception.*;

@RestController
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/vouchers")
    public List<Voucher> findAll() {
        return this.voucherService.findAll();
    }

    @GetMapping("/vouchers/{code}")
    public ResponseEntity<Voucher> findByCode(@PathVariable String code) {
        Optional<Voucher> optVoucher = this.voucherService.findByCode(code);

        if (optVoucher.isPresent()) {
            return new ResponseEntity<>(optVoucher.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/vouchers")
    public Voucher save(@RequestBody Voucher voucher) {
        return this.voucherService.save(voucher);
    }

    @DeleteMapping("/vouchers/{code}")
    public void delete(@PathVariable String code) {
        this.voucherService.delete(code);
    }

    @PutMapping("/vouchers/{code}")
    public void useVoucher(@PathVariable String code) {
        this.voucherService.useVoucher(code);
    }


    @ExceptionHandler(NoVoucherExistsException.class)
    public ResponseEntity<ApiError> handleException(NoVoucherExistsException ex) {
        ApiError apiError = new ApiError( HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VoucherAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleException(VoucherAlreadyExistsException ex) {
        ApiError apiError = new ApiError( HttpStatus.CONFLICT.value(), ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
