package org.gdo.voucherio.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Voucher {

    private String code;
    private VoucherDuration duration;
    private Boolean isUsed;

}
