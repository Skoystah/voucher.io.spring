package org.gdo.voucherio.voucher.repository;

import java.util.List;
import java.util.Optional;

import org.gdo.voucherio.voucher.model.Voucher;
import org.gdo.voucherio.voucher.service.VoucherSearchCriteria;

public interface JdbcVoucherRepository {

    public Voucher save(Voucher voucher);

    public void delete(String code);

    public List<Voucher> findAll(VoucherSearchCriteria voucherSearchCriteria);

    public Optional<Voucher> findByCode(String code);

    public void useVoucher(String code);
}
