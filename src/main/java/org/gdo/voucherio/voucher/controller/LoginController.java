package org.gdo.voucherio.voucher.controller;

import org.gdo.voucherio.voucher.model.User;
import org.gdo.voucherio.voucher.model.UserResponse;
import org.gdo.voucherio.voucher.model.UserResponseMapper;
import org.gdo.voucherio.voucher.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserResponseMapper userResponseMapper;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserResponseMapper userResponseMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        final Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.name, loginRequest.password);
        final Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        final String jwt = this.jwtService.generateToken(authenticationResponse.getName());
        final User authUser = (User) authenticationResponse.getPrincipal();

        final ResponseCookie authCookie = ResponseCookie.from("authToken")
                .httpOnly(true)
                .secure(true)
                .value(jwt)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .body(userResponseMapper.from(authUser));
    }

    public record LoginRequest(String name, String password) {
    }
}
