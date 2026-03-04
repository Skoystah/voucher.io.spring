package org.gdo.voucherio.voucher.service;

import org.gdo.voucherio.voucher.model.Voucher;
import org.gdo.voucherio.voucher.model.VoucherDuration;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class VoucherSearchCriteria {

    @Nullable
    private String duration;

    private Boolean includeUsed = false;

    public VoucherSearchCriteria(String duration, Boolean includeUsed) {
        this.duration = duration;

        if (includeUsed != null && includeUsed == true) {
            this.includeUsed = true;
        }
    }

    public Voucher toVoucher() {
        Voucher voucher = new Voucher();
        if (this.duration != null) {
            voucher.setDuration(VoucherDuration.fromCode(this.duration));
        }

        if (!this.includeUsed) {
            voucher.setUsed(false);
        }

        return voucher;
    }
}
