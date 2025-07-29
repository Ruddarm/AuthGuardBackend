package com.authguard.authguard.controller;

import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.services.AuthService;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserInfo {

    private final AuthService authService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> userinfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, UsernameNotFoundException, ExpiredJwtException {
        System.out.println("Inside user info");
        final String tokenHeader = request.getHeader("Authorization");
        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                System.out.println("Header is null or something in Client Jwt Filter");
            }
            String token = tokenHeader.split("Bearer ")[1];
            Map<String, Object> data = authService.getUserinfo(token);
            return ResponseEntity.ok(data);

        } catch (ExpiredJwtException ex) {
            System.out.println(ex);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Token is expired\"}");
            response.getWriter().flush();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + ex.getMessage());

        }
    }

}
