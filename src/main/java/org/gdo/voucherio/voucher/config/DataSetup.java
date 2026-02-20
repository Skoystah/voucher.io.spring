package org.gdo.voucherio.voucher.config;

import java.util.Date;

import org.gdo.voucherio.voucher.model.User;
import org.gdo.voucherio.voucher.model.Voucher;
import org.gdo.voucherio.voucher.model.VoucherDuration;
import org.gdo.voucherio.voucher.repository.UserRepository;
import org.gdo.voucherio.voucher.repository.VoucherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(
            VoucherRepository voucherRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
            ) {
        return args -> {
            voucherRepository.save(new Voucher("LEU123", VoucherDuration.ONE_HOUR, false));
            voucherRepository.save(new Voucher("LEU456",VoucherDuration.TWELVE_HOURS, false));
            voucherRepository.save(new Voucher("LEU789", VoucherDuration.FOUR_HOURS, true));
            voucherRepository.save(new Voucher("LEUABC", VoucherDuration.TWO_HOURS, false));
            userRepository.save(new User("Jos", true, new Date().toString(), passwordEncoder.encode("password") ));
        };
    }
}
