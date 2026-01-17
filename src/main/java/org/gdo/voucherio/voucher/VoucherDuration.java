package org.gdo.voucherio.voucher;

public enum VoucherDuration {
    ONE_HOUR("1h"),
    TWO_HOURS("2h"),
    FOUR_HOURS("4h"),
    TWELVE_HOURS("12h");

    public String duration;

    private VoucherDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return this.duration;
    }

    public static VoucherDuration fromCode(String duration) {
        for (VoucherDuration d : values()) {
            if (d.duration.equals(duration)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Duration does not exist");
    }
}
