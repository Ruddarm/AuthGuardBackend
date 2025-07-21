package com.authguard.authguard.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.AppRequest;
import com.authguard.authguard.model.dto.AppResponse;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.mapper.AppMapper;
import com.authguard.authguard.services.AppService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
public class AppController {

    public final AppService appService;

    @PutMapping("/app/{clientId}")
    public ResponseEntity<AppResponse> putMethodName(@PathVariable UUID clientId,
            @Valid @RequestBody AppRequest appRequest) throws ResourceException {
        AppEntity app = appService.createApp(AppMapper.toAppEntity(appRequest), clientId);
        return new ResponseEntity<>(AppMapper.toAppResponse(app), HttpStatus.CREATED);
    }

    @GetMapping("app/apiKey/{appId}")
    public String putMethodName(@PathVariable UUID appId) throws ResourceException {
        return appService.generateApiKey(appId);
    }

}
