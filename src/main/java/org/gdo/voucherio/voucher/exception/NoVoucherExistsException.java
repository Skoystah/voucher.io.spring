package org.gdo.voucherio.voucher.exception;

public class NoVoucherExistsException extends RuntimeException {
    public NoVoucherExistsException(String message) {
        super(message);
    }
}
