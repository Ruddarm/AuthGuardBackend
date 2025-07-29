package com.authguard.authguard.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.AuthUser;
import com.authguard.authguard.model.domain.UserType;
import com.authguard.authguard.model.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

//for id token

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.authguard.authguard.model.entity.UserEntity;

@Data
@Service
public class JwtService {

    public static final String SecretJwtKey = "ddgdbydjsmsjjsmhdgdndjsksjbdddjdkddk";
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtService() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
            this.publicKey = (RSAPublicKey) keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA key pair", e);
        }
    }

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

    public String createOauth2AccessToken(String userId, String clientId) {
        return Jwts.builder().subject("userId").claim("client_id", clientId).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 30))
                .signWith(generateSecretKey())
                .compact();
    }

    public String createreOauth2refreshToken(String userId, String clientId) {
        return Jwts.builder().subject("userId").claim("client_id", clientId).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 30))
                .signWith(generateSecretKey())
                .compact();
    }

    public String createIdToken(String nonce, UserEntity user, String clientId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .issuer("http://localhost:8081")
                .subject(user.getUserId().toString())
                .claim("aud", clientId).claim("nonce", nonce)
                .claim("email", user.getEmail()).claim("name", user.getFirstName() + " " + user.getLastName())
                .issuedAt(new Date(now))
                .expiration(new Date(now + 1000 * 60 * 5))
                .signWith(privateKey, Jwts.SIG.RS256) // ✅ This is the modern way
                .compact();

    }

    // public String createToken(AuthUser authUser, AppEntity app) {
    // return Jwts.builder().subject(authUser.getUserId().toString()).claim("email",
    // authUser.getUsername())
    // .claim("userType", authUser.getUserType())
    // .claim("appId", app.getAppId())
    // .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 *
    // 60))
    // .signWith(generateSecretKey(app.getApiKeyEntity().getApiKey().toString()))
    // .compact();
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
    // return Jwts.builder().subject(authUser.getUserId().toString()).issuedAt(new
    // Date())
    // .claim("appId", app.getAppId())
    // .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
    // .signWith(generateSecretKey(app.getApiKeyEntity().getApiKey().toString()))
    // .compact();
    // }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(generateSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    // public Claims getClaims(String token, AppEntity app) {
    // return
    // Jwts.parser().verifyWith(generateSecretKey()).build().parseSignedClaims(token).getPayload();
    // }

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

    public UUID extractClientIDFromToken(String token) {
        Claims claims = getClaims(token);
        String clintIdString = claims.get("client_id", String.class); // Assuming you're storing userId in subject
        try {
            return UUID.fromString(clintIdString);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format in token");
        }
    }

}
