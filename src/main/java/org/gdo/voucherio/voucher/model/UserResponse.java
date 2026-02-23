package org.gdo.voucherio.voucher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private String name;
    private Boolean is_admin;

}
