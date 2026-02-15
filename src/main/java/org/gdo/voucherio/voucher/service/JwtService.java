package org.gdo.voucherio.voucher.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {

        Instant issued_at = Instant.now();
        // Todo for now 30 days - to parametrize
        Instant expires_at = issued_at.plus(30, ChronoUnit.DAYS);

        String jwt = Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(issued_at))
                .expiration(Date.from(expires_at))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();

        System.out.println("jwt " + jwt);
        return jwt;

    }

    public SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateJwt(String jwt) {

        // add logic
        return true;

    }

    public String extractUserName(String jwt) {
        return extractClaims(jwt).getSubject();
    }

    private Claims extractClaims(String jwt) {

        Jws<Claims> parse = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(jwt);

        return parse.getPayload();
    }

}
