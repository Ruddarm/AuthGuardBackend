package com.authguard.authguard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.domain.AuthUser;
import com.authguard.authguard.model.domain.UserAuth;
import com.authguard.authguard.model.dto.ClientAppRequest;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.services.AppUserAuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final AppUserAuthService appUserAuthService;

    @GetMapping("")
    public String getFuckName() {
        return "Name is Fuck Man";
    }

    @PostMapping("/app/oath/login")
    public ResponseEntity<UserResponse> linkUserToApp(
            @Valid @RequestBody ClientAppRequest clientAppRequest,
            @AuthenticationPrincipal UserAuth userAuth,
            HttpServletResponse response) throws ResourceException, UsernameNotFoundException {

        System.out.println(userAuth.getUsername());
        String[] data = appUserAuthService.authenticate(clientAppRequest, userAuth);

        String cookie = String.format(
                "refresh_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                data[1], 7 * 24 * 60 * 60);
        response.setHeader("Set-Cookie", cookie);

        return ResponseEntity.ok(
                UserResponse.builder()
                        .accessToken(data[0])
                        .firstName(data[3])
                        .lastName(data[4])
                        .email(data[5])
                        .build());
    }

}
