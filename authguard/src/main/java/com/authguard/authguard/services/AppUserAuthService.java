package com.authguard.authguard.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.domain.AuthUser;
import com.authguard.authguard.model.dto.AuthCodePayload;
import com.authguard.authguard.model.dto.ClientUserLoginRequest;
import com.authguard.authguard.model.dto.CodePayload;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.UserAppLinkEntity;
import com.authguard.authguard.model.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class AppUserAuthService {
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final UserAppLinkService userAppLinkService;
        private final AppService appService;
        private final UserService userService;
        private final RedisService redisService;
        private static final SecureRandom random = new SecureRandom();
        private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

        public AppUserAuthService(
                        JwtService jwtService,
                        @Qualifier("userAuthManager") AuthenticationManager authenticationManager,
                        UserAppLinkService userAppLinkService,
                        AppService appService,
                        UserService userService,
                        RedisService redisService) {
                this.jwtService = jwtService;
                this.authenticationManager = authenticationManager;
                this.userAppLinkService = userAppLinkService;
                this.appService = appService;
                this.userService = userService;
                this.redisService = redisService;
        }

        // public String generateCode(UUID clientId, UUID appId, UUID userId) throws
        // ResourceException {
        // AppEntity app = appService.validateApp(appId, clientId);
        // if (app != null) {
        // return generate();
        // } else {
        // throw new ResourceException("App not found");
        // }
        // }

        public String authenticateAndGenerateCode(ClientUserLoginRequest clientUserLoginRequest)
                        throws ResourceException, UsernameNotFoundException, JsonProcessingException {
                UUID appId;
                UUID clientId;
                try {
                        appId = UUID.fromString(clientUserLoginRequest.getAppId());
                        clientId = UUID.fromString(clientUserLoginRequest.getClientId());
                        AppEntity app = appService.validateApp(appId, clientId);
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(clientUserLoginRequest.getUsername(),
                                                        clientUserLoginRequest.getPassword()));
                        UserEntity user = userService.findByEmail(clientUserLoginRequest.getUsername())
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found "));
                        UserAppLinkEntity appLinkEntity = userAppLinkService.getApplink(app, user);
                        String authCode = generate();
                        redisService.saveAuthCode(authCode, AuthCodePayload.builder().clientId(clientId).appId(appId)
                                        .userId(user.getUserId())
                                        .appUserLinkId(appLinkEntity == null ? null : appLinkEntity.getLinkID())
                                        .build());
                        // AuthUser authUser = (AuthUser) authentication.getPrincipal();
                        // String accessToken = jwtService.createToken(authUser, app);
                        // String refreshToken = jwtService.refreshToken(authUser, app);
                        return authCode;
                } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid UUID format in token");
                }
        }

        public String genterateCode(UUID clientId, UUID appId, UUID userId)
                        throws ResourceException, UsernameNotFoundException, JsonProcessingException {
                AppEntity app = appService.validateApp(appId, clientId);
                UserEntity user = userService.findById(userId)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found "));
                UserAppLinkEntity appLinkEntity = userAppLinkService.getApplink(app, user);
                String authCode = generate();
                redisService.saveAuthCode(authCode, AuthCodePayload.builder().clientId(clientId).appId(appId)
                                .userId(user.getUserId())
                                .appUserLinkId(appLinkEntity == null ? null : appLinkEntity.getLinkID())
                                .build());
                return authCode;

        }

        public UserResponse validateCode(CodePayload codePayload) throws JsonProcessingException, ResourceException {
                AuthCodePayload authCodePayload = redisService.getAuthCodePayLoad(codePayload.getCode());
                if (authCodePayload == null) {
                        System.out.println("Authcode is null " + authCodePayload);
                        throw new ResourceException("Code is invalid or expired");
                }
                if (!authCodePayload.getAppId().equals(codePayload.getAppId()))
                        throw new ResourceException("client Id did not matched");
                if (!authCodePayload.getClientId().equals(codePayload.getClientId()))
                        throw new ResourceException("App Id did not matched");
                AppEntity app = appService.validateApp(codePayload.getAppId(), codePayload.getClientId());
                UserEntity user = userService.findById(authCodePayload.getUserId())
                                .orElseThrow(() -> new ResourceException("User not find"));
                if (!app.getApiKeyEntity().getApiKey().equals(codePayload.getApiKey()))
                        throw new ResourceException("Invalid api Key");
                if (authCodePayload.getAppUserLinkId() == null) {
                        userAppLinkService.linkUserApp(app, user);
                }
                return UserResponse.builder().firstName(user.getFirstName()).lastName(user.getLastName())
                                .email(user.getEmail()).build();

        }
        // public String[] authenticate(ClientAppRequest clientAppRequest, AuthUser
        // authUser)
        // throws ResourceException, UsernameNotFoundException {
        // AppEntity app = appService.validateApp(clientAppRequest.getAppId(),
        // clientAppRequest.getClientId());
        // UserEntity user = userService.findByEmail(authUser.getUsername())
        // .orElseThrow(() -> new UsernameNotFoundException(" User not found"));
        // userAppLinkService.linkUserApp(app, user);
        // String accessToken = jwtService.createToken(authUser, app);
        // String refreshToken = jwtService.refreshToken(authUser, app);
        // return new String[] { accessToken, refreshToken,
        // authUser.getUserId().toString(), user.getFirstName(),
        // user.getLastName(), user.getEmail() };
        // }

        public static String generate() {
                byte[] bytes = new byte[24];
                random.nextBytes(bytes);
                return encoder.encodeToString(bytes);
        }

}
