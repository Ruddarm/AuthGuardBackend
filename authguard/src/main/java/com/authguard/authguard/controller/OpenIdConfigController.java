package com.authguard.authguard.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.services.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/.well-known")
public class OpenIdConfigController {
    @Autowired
    private JwtService jwtService;

    @GetMapping("/openid-configuration")
    public Map<String, Object> openIdConfiguration(HttpServletRequest request) {
        String issuer = "http://localhost:8080"; // Replace with dynamic host if needed
        System.out.println("fucking here");
        Map<String, Object> config = new HashMap<>();
        config.put("issuer", issuer);
        config.put("authorization_endpoint", issuer + "/oauth2/authorize");
        config.put("token_endpoint", issuer + "/oauth2/token");
        config.put("userinfo_endpoint", issuer + "/userinfo");
        config.put("jwks_uri", issuer + "/.well-known/jwks.json");
        config.put("response_types_supported", List.of("code"));
        config.put("subject_types_supported", List.of("public"));
        config.put("id_token_signing_alg_values_supported", List.of("RS256"));
        return config;
    }

    @GetMapping("/jwks.json")
    public Map<String, Object> getJwks() {
        RSAPublicKey publicKey = jwtService.getPublicKey();

        Map<String, Object> jwk = new HashMap<>();
        jwk.put("kty", "RSA");
        jwk.put("alg", "RS256");
        jwk.put("use", "sig");
        jwk.put("n", Base64.getUrlEncoder().encodeToString(publicKey.getModulus().toByteArray()));
        jwk.put("e", Base64.getUrlEncoder().encodeToString(publicKey.getPublicExponent().toByteArray()));
        jwk.put("kid", "authguard-key"); // key ID
        return Map.of("keys", List.of(jwk));
    }

}
