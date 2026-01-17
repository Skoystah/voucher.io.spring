package org.gdo.voucherio.voucher;

import java.util.List;
import java.util.Optional;

public interface VoucherService {

    public List<Voucher> findAll();

    public Optional<Voucher> findByCode(String code);

    public Voucher save(Voucher voucher);

    public void delete(String code);

    public void useVoucher(String code);
}
