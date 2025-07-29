package com.authguard.authguard.controller;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.CodePayload;
import com.authguard.authguard.services.AppUserAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class oauth2Controller {

    private final AppUserAuthService appUserAuthService;

    @PostMapping("/token")
    public ResponseEntity<?> exchangeTokenCode(@RequestParam Map<String, String> params,
            @RequestHeader HttpHeaders headers)
            throws JsonProcessingException, ResourceException {
        String grantType = params.get("grant_type");
        String code = params.get("code");
        // System.out.println("code is code " + code);
        String redirectUri = params.get("redirect_uri");
        // String clientId = params.get("client_id");
        // System.out.println("client Id "+clientId);
        // String clientSecret = params.get("client_secret");
        String authHeader = headers.getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "missing_authorization_header"));
        }

        String base64Credentials = authHeader.substring("Basic ".length());
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String[] clientCredentials = new String(decoded).split(":", 2);
        if (clientCredentials.length != 2) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid_basic_auth_format"));
        }
        String clientId = clientCredentials[0];
        String clientSecret = clientCredentials[1];
        UUID client_id;
        try {
            client_id = UUID.fromString(clientId);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid client Id");
            return ResponseEntity.badRequest().body(Map.of("error", "invalid_client_id_format"));
        }
        if (!"authorization_code".equals(grantType)) {
            System.out.println("Invalid grant type");
            return ResponseEntity.badRequest().body(Map.of("error", "unsupported_grant_type"));
        }
        String[] tokens = appUserAuthService.validateCode(
                CodePayload.builder().code(code).client_id(client_id).client_secret(clientSecret).build());
        // System.out.println("fuking ok");

        return ResponseEntity.ok(Map.of(
                "access_token", tokens[0],
                "token_type", "Bearer",
                "expires_in", 3600,
                "refresh_token", tokens[1],
                "id_token", tokens[2],
                "scope", "read write"));
    }

    @GetMapping("/authorize")
    public RedirectView loginredirct(@RequestParam String client_id,
            @RequestParam String redirect_uri, @RequestParam String state, @RequestParam String nonce) {
        String loginUrl = "http://localhost:5173/oauth/user/login" + "?client_id=" + client_id +
                "&redirectUrl=" + redirect_uri + "&state=" + state + "&nonce=" + nonce;
        return new RedirectView(loginUrl);
    }

}
