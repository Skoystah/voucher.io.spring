package org.gdo.voucherio.voucher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherResponse {

    private String code;
    private String duration;
    private Boolean used;

}
