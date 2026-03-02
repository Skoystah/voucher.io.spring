package org.gdo.voucherio.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.gdo.voucherio.voucher.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    // @Test
    public void testGenerateJwt() {

        final String userName = "Jos";

        assertNotNull(jwtService.generateToken(userName));

    }

}
