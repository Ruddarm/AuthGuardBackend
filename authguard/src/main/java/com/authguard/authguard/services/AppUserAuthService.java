package com.authguard.authguard.services;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.domain.AuthUser;
import com.authguard.authguard.model.dto.ClientUserLoginRequest;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.UserAppLinkEntity;
import com.authguard.authguard.model.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserAuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAppLinkService userAppLinkService;
    private final AppService appService;
    private final UserService userService;

    public String[] authenticate(ClientUserLoginRequest clientUserLoginRequest)
            throws ResourceException, UsernameNotFoundException {
        UUID appId;
        UUID clientId;
        try {
            appId = UUID.fromString(clientUserLoginRequest.getAppId());
            clientId = UUID.fromString(clientUserLoginRequest.getClientId());
            AppEntity app = appService.validateApp(appId, clientId);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(clientUserLoginRequest.getUsername(),
                            clientUserLoginRequest.getPassword()));
            userAppLinkService.linkUserApp(app, userService.findByEmail(clientUserLoginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found ")));
            AuthUser authUser = (AuthUser) authentication.getPrincipal();
            String accessToken = jwtService.createToken(authUser,app);
            String refreshToken = jwtService.refreshToken(authUser,app);
            return new String[] { accessToken, refreshToken, authUser.getUserId().toString() };
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format in token");
        }
    }
}
