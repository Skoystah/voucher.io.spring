package org.gdo.voucherio.voucher.model;

import jakarta.persistence.EnumeratedValue;

public enum VoucherDuration {
    ONE_HOUR("1h"),
    TWO_HOURS("2h"),
    FOUR_HOURS("4h"),
    TWELVE_HOURS("12h");

    @EnumeratedValue
    public String strDuration;

    private VoucherDuration(String duration) {
        this.strDuration = duration;
    }

    public String toStr() {
        return this.strDuration;
    }

    public static VoucherDuration fromCode(String duration) {
        for (VoucherDuration d : values()) {
            if (d.strDuration.equals(duration)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Duration does not exist");
    }
}
