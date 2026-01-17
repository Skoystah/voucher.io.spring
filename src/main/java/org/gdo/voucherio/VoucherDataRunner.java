package org.gdo.voucherio;

import org.gdo.voucherio.voucher.Voucher;
import org.gdo.voucherio.voucher.VoucherDuration;
import org.gdo.voucherio.voucher.VoucherRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class VoucherDataRunner implements ApplicationRunner {

    private final VoucherRepository voucherRepository;

    public VoucherDataRunner(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // this.voucherRepository.save(new Voucher("LEU123", VoucherDuration.ONE_HOUR,
        // false));
        // this.voucherRepository.save(new Voucher("LEU456",
        // VoucherDuration.TWELVE_HOURS, false));
        // this.voucherRepository.save(new Voucher("LEU789", VoucherDuration.TWO_HOURS,
        // false));
        // this.voucherRepository.save(new Voucher("LEU135", VoucherDuration.FOUR_HOURS,
        // false));
    }

}
