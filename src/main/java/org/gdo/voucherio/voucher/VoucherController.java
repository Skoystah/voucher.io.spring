package org.gdo.voucherio.voucher;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoucherController {

    private final VoucherRepository voucherRepository;

    public VoucherController(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @GetMapping("/vouchers")
    public List<Voucher> getVouchers() {
        return this.voucherRepository.getVouchers();
    }

    @GetMapping("/vouchers/{code}")
    public Voucher getVoucher(@PathVariable String code) {
        return this.voucherRepository.getVoucher(code);
    }

    @PostMapping("/vouchers")
    public Voucher save(@RequestBody Voucher voucher) {
        return this.voucherRepository.save(voucher);
    }
}
