package com.authguard.authguard.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.ClientUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final String SecretJwtKey = "ddgdbydjsmsjjsmhdgdndjsksjbdddjdkddk";

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(SecretJwtKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(ClientUser clientUser) {
        return Jwts.builder().subject(clientUser.getUserId().toString()).claim("email", clientUser.getUsername())
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(generateSecretKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(generateSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public UUID generateUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        String userIdString = claims.getSubject(); // Assuming you're storing userId in subject

        try {
            return UUID.fromString(userIdString);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format in token");
        }
    }

}
