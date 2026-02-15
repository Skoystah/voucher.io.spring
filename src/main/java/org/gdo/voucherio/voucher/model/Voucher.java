package org.gdo.voucherio.voucher.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public final class Voucher {

    @Id
    private String code;

    @Enumerated(EnumType.STRING)
    private VoucherDuration duration;

    private Boolean used;

}
