package org.gdo.voucherio.voucher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;

@Configuration
public class LogoutConfig {

    @Bean
    public CookieClearingLogoutHandler cookieClearing() {
        return new CookieClearingLogoutHandler("authToken");
    }

}
