package org.gdo.voucherio.voucher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class VoucherRepository {

    public final Map<String, Voucher> vouchers = new HashMap<>();

    public Voucher save(Voucher voucher) {
        vouchers.put(voucher.getCode(), voucher);
        return voucher;
    }

    public List<Voucher> getVouchers() {
        List<Voucher> retrievedVouchers = new ArrayList<>(vouchers.values());
        return retrievedVouchers;
    }

    public Voucher getVoucher(String code) {
        return vouchers.get(code);
    }

}
