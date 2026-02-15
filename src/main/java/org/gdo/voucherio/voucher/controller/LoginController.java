package org.gdo.voucherio.voucher.controller;

import java.net.HttpCookie;

import org.gdo.voucherio.voucher.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.name, loginRequest.password);
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        String jwt = this.jwtService.generateToken(authenticationResponse.getName());
        ResponseCookie authCookie = ResponseCookie.from("authToken")
                .httpOnly(true)
                .secure(true)
                .value(jwt)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("authToken") String authToken) {

        ResponseCookie authCookie = ResponseCookie.from("authToken")
                .build();

        System.out.println("logging out!" + authToken);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .body(null);
    }

    public record LoginRequest(String name, String password) {
    }

    private String extractJwtFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("authToken")) {
                    return c.getValue();
                }
            }
        }

        return null;
    }
}
