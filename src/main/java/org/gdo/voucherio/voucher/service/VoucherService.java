package org.gdo.voucherio.voucher.service;

import java.util.List;

import org.gdo.voucherio.voucher.model.VoucherRequest;
import org.gdo.voucherio.voucher.model.VoucherResponse;

public interface VoucherService {

    public List<VoucherResponse> findAll(VoucherSearchCriteria voucherSearchCriteria);

    public VoucherResponse findByCode(String code);

    public VoucherResponse addVoucher(VoucherRequest voucher);

    public void delete(String code);

    public void useVoucher(String code);
}
