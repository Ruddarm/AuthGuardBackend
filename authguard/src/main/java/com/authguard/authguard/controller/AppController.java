package com.authguard.authguard.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.AppRequest;
import com.authguard.authguard.model.dto.AppResponse;
import com.authguard.authguard.model.dto.AppSummary;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.mapper.AppMapper;
import com.authguard.authguard.services.AppService;
import com.authguard.authguard.services.UserAppLinkService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
public class AppController {

    public final AppService appService;
    public final UserAppLinkService userAppLinkService;

    // get list of appp
    @GetMapping("/app/summary/{clientId}")
    public ResponseEntity<List<AppSummary>> getAppSummary(@PathVariable String clientId) throws ResourceException {
        return new ResponseEntity<>(appService.getAppSummary(clientId), HttpStatus.OK);
    }

    // get particular app
    @GetMapping("/app/")
    public ResponseEntity<AppResponse> getApp(@RequestParam UUID client_id)
            throws ResourceException {
        AppEntity appEntity = appService.validateApp(client_id);
        return ResponseEntity
                .ok(AppResponse.builder().appName(appEntity.getAppName()).client_Id(client_id)
                        .client_secret(appEntity.getClient_secret()).build());
    }

    // get user of aprticualr app
    @GetMapping("/app/user/{appId}")
    public ResponseEntity<List<UserResponse>> getUserOfApp(@PathVariable UUID appId) {
        return ResponseEntity.ok(userAppLinkService.getListOfUserByApp(appId));
    }

    // create app route
    @PostMapping("/app/{clientId}")
    public ResponseEntity<AppResponse> createApp(@PathVariable UUID clientId,
            @Valid @RequestBody AppRequest appRequest) throws ResourceException {
        AppEntity app = appService.createApp(AppMapper.toAppEntity(appRequest), clientId);
        return new ResponseEntity<>(AppMapper.toAppResponse(app), HttpStatus.CREATED);
    }

    // get app api key
    // @GetMapping("app/apiKey/{appId}")
    // public String getApiKey(@PathVariable UUID appId) throws ResourceException {
    // return appService.generateApiKey(appId);
    // }

    @DeleteMapping("app/{appId}")
    public ResponseEntity<?> deleteApp(@PathVariable UUID appId) {
        appService.deleteApp(appId);
        return ResponseEntity.ok("");
    }

    // delete app
    public ResponseEntity<?> deleteApp() {
        return ResponseEntity.ok("Deleted App");
    }
}
