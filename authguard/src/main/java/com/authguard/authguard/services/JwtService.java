package com.authguard.authguard.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.AuthUser;
import com.authguard.authguard.model.domain.UserType;
import com.authguard.authguard.model.entity.AppEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final String SecretJwtKey = "ddgdbydjsmsjjsmhdgdndjsksjbdddjdkddk";

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(SecretJwtKey.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey generateSecretKey(String apiKey) {
        return Keys.hmacShaKeyFor(apiKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(AuthUser authUser) {
        return Jwts.builder().subject(authUser.getUserId().toString()).claim("email", authUser.getUsername())
                .claim("userType", authUser.getUserType())
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .signWith(generateSecretKey())
                .compact();
    }

    // public String createToken(AuthUser authUser, AppEntity app) {
    //     return Jwts.builder().subject(authUser.getUserId().toString()).claim("email", authUser.getUsername())
    //             .claim("userType", authUser.getUserType())
    //             .claim("appId", app.getAppId())
    //             .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60))
    //             .signWith(generateSecretKey(app.getApiKeyEntity().getApiKey().toString()))
    //             .compact();
    // }

    public String refreshToken(AuthUser authUser) {
        System.out.println("User type is " + authUser.getUserType());
        return Jwts.builder().subject(authUser.getUserId().toString()).claim("userType", authUser.getUserType())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(generateSecretKey())
                .compact();
    }

    // public String refreshToken(AuthUser authUser, AppEntity app) {
    //     return Jwts.builder().subject(authUser.getUserId().toString()).issuedAt(new Date())
    //             .claim("appId", app.getAppId())
    //             .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
    //             .signWith(generateSecretKey(app.getApiKeyEntity().getApiKey().toString()))
    //             .compact();
    // }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(generateSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public Claims getClaims(String token, AppEntity app) {
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

    public UserType extractUserType(String token) {
        Claims claims = getClaims(token);
        String userTypeString = claims.get("userType", String.class);
        System.out.println("User Type String " + userTypeString);
        try {
            return UserType.valueOf(userTypeString);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid userType in token: " + userTypeString);
        }
    }

}
