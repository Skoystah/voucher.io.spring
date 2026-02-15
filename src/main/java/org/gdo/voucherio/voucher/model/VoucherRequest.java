package org.gdo.voucherio.voucher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherRequest {

    private String code;
    private String duration;

}
