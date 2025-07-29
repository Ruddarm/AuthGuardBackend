package com.authguard.authguard.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.domain.UserAuth;
import com.authguard.authguard.model.dto.AuthCodePayload;
import com.authguard.authguard.model.dto.ClientAppRequest;
import com.authguard.authguard.services.AppUserAuthService;
import com.authguard.authguard.services.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final RedisService redisService;
    private final AppUserAuthService appUserAuthService;

    @GetMapping("")
    public String getFuckName() {
        return "Name is Fuck Man";
    }

    @PostMapping("/app/code")
    public ResponseEntity<Map<String, String>> getCodeForApp(@Valid @RequestBody ClientAppRequest clientAppRequest,
            @AuthenticationPrincipal UserAuth userAuth) throws ResourceException, JsonProcessingException {

        String code = appUserAuthService.genterateCode(clientAppRequest.getClient_id(), clientAppRequest.getNonce(),
                userAuth.getUserId());
        // redisService.saveAuthCode(code,
        // AuthCodePayload.builder().client_id(clientAppRequest.getClient_id())
        // .userId(userAuth.getUserId()).build());
        URI redirectUrl = URI.create(clientAppRequest.getRedirectUrl() + "?code=" + code);
        System.out.println(redirectUrl);
        // return ResponseEntity.status(HttpStatus.FOUND).location(redirectUrl).build();
        Map<String, String> response = new HashMap<>();
        response.put("code", code);
        response.put("redirecturi", clientAppRequest.getRedirectUrl());

        return ResponseEntity.ok(response);

    }

    // @PostMapping("/app/oath/login")
    // public ResponseEntity<String> linkUserToApp(
    // @Valid @RequestBody ClientAppRequest clientAppRequest,
    // @AuthenticationPrincipal UserAuth userAuth,
    // HttpServletResponse response) throws ResourceException,
    // UsernameNotFoundException {

    // System.out.println(userAuth.getUsername());
    // String[] data = appUserAuthService.authenticate(clientAppRequest, userAuth);

    // String cookie = String.format(
    // "refresh_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
    // data[1], 7 * 24 * 60 * 60);
    // response.setHeader("Set-Cookie", cookie);

    // return ResponseEntity.ok(
    // UserResponse.builder()
    // .accessToken(data[0])
    // .firstName(data[3])
    // .lastName(data[4])
    // .email(data[5])
    // .build());
    // }

}
