package org.gdo.voucherio.voucher;

import java.util.List;
import java.util.Optional;

import org.gdo.voucherio.voucher.exception.NoVoucherExistsException;
import org.gdo.voucherio.voucher.exception.VoucherAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public List<Voucher> findAll() {
        return this.voucherRepository.findAll();
    }

    public Optional<Voucher> findByCode(String code) {
        return this.voucherRepository.findByCode(code);
    }

    public Voucher save(Voucher voucher) {
        Optional<Voucher> savedVoucher = this.voucherRepository.findByCode(voucher.getCode());

        if (savedVoucher.isPresent()) {
            throw new VoucherAlreadyExistsException(
                    String.format("Voucher with code %s already exists", voucher.getCode()));
        }

        voucher.setUsed(false);
        return this.voucherRepository.save(voucher);
    }

    public void delete(String code) {
        Optional<Voucher> voucher = this.voucherRepository.findByCode(code);

        if (!voucher.isPresent()) {
            throw new NoVoucherExistsException("Voucher does not exist");
        }

        this.voucherRepository.delete(code);
    }

    public void useVoucher(String code) {
        Optional<Voucher> voucher = this.voucherRepository.findByCode(code);

        if (!voucher.isPresent()) {
            throw new NoVoucherExistsException("Voucher does not exist");
        }

        this.voucherRepository.useVoucher(code);
    }
}
