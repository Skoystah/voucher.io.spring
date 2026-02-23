package org.gdo.voucherio.voucher.config;

import java.io.IOException;
import org.gdo.voucherio.voucher.service.JwtService;
import org.gdo.voucherio.voucher.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Already authenticated!
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromCookie(request);

        if (jwt != null && this.jwtService.isValid(jwt)) {
            String username = this.jwtService.extractUserName(jwt);

            // Load user from repository based on the jwt user name
            try {

                UserDetails currentUser = userService.loadUserByUsername(username);
                // Create authentication - based on jwt token
                UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.authenticated(
                        currentUser,
                        null,
                        currentUser.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception e) {
                log.error("Error retrieving user details: ", e);
            }
        }

        filterChain.doFilter(request, response);
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
