package org.gdo.voucherio.voucher.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@ConfigurationProperties(prefix = "jwt")
@Slf4j
public class JwtService {

    private String secretKey;

    public void setSecretKey(String secretKey) {
        log.debug("SETTING SECRET KEY WITH " + secretKey);
        this.secretKey = secretKey;
    }

    public String generateToken(String username) {

        final Instant issued_at = Instant.now();
        // Todo for now 30 days - to parametrize
        final Instant expires_at = issued_at.plus(30, ChronoUnit.DAYS);

        final String jwt = Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(issued_at))
                .expiration(Date.from(expires_at))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();

        log.debug("Generated jwt token: " + jwt);
        return jwt;

    }

    public SecretKey getSignKey() {
        final byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isValid(String jwt) {

        try {
            if (isExpired(extractExpiration(jwt))) {
                return false;
            }
        } catch (Exception e) {
            log.warn("JWT error : ", e.getMessage());
            return false;
        }
        // add logic
        return true;

    }

    public String extractUserName(String jwt) {
        return extractClaims(jwt).getSubject();
    }

    public Date extractExpiration(String jwt) {
        return extractClaims(jwt).getExpiration();
    }

    private Boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private Claims extractClaims(String jwt) {
        final Jws<Claims> parse = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(jwt);
        return parse.getPayload();
    }

}
