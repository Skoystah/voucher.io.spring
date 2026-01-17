package org.gdo.voucherio.voucher.exception;

public class VoucherAlreadyExistsException extends RuntimeException {

    private String message;

    public VoucherAlreadyExistsException() {
    }

    public VoucherAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

}
