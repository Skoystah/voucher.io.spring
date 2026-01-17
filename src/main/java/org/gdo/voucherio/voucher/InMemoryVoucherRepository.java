package org.gdo.voucherio.voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryVoucherRepository implements VoucherRepository {

    // Map is not concurrent safe!! Use concurrent hashmap or replace by DB
    // private final Map<String, Voucher> vouchers = new HashMap<>();
    private final Map<String, Voucher> vouchers = new ConcurrentHashMap<>();

    public Voucher save(Voucher voucher) {
        vouchers.put(voucher.getCode(), voucher);
        return voucher;
    }

    public void delete(String code) {
        vouchers.remove(code);
    }

    public List<Voucher> findAll() {
        List<Voucher> retrievedVouchers = new ArrayList<>(vouchers.values());
        return retrievedVouchers;
    }

    public Optional<Voucher> findByCode(String code) {
        Voucher voucher = vouchers.get(code);
        return voucher == null ? Optional.empty() : Optional.of(voucher);
    }

    public void useVoucher(String code) {
        Voucher voucher = vouchers.get(code);
        voucher.setUsed(true);
        // return voucher;
    }

}
