package com.authguard.authguard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.CodePayload;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.services.AppUserAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/endpoint")
@RequiredArgsConstructor
public class EnpointController {
    private final AppUserAuthService appUserAuthService;

    @GetMapping("/app/name/{appId}")
    public String getAppName(@PathVariable String param) {
        return new String();
    }


    @PostMapping("/oath/token/verify")
    public ResponseEntity<UserResponse> verifyToken(@Valid @RequestBody CodePayload param)
            throws JsonProcessingException, ResourceException {
        return ResponseEntity.ok(appUserAuthService.validateCode(param));
    }

}
