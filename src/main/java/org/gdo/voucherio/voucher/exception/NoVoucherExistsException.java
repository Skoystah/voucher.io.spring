package org.gdo.voucherio.voucher.exception;

public class NoVoucherExistsException extends RuntimeException {

    private String message;

    public NoVoucherExistsException() {
    }

    public NoVoucherExistsException(String message) {
        super(message);
        this.message = message;
    }
}
