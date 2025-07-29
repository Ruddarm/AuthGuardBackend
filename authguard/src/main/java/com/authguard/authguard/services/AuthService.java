package com.authguard.authguard.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.AuthUser;
import com.authguard.authguard.model.domain.UserType;
import com.authguard.authguard.model.dto.LoginRequest;
import com.authguard.authguard.model.entity.UserEntity;

@Service
public class AuthService {

    private final JwtService jwtService;
    @Qualifier("clientAuthManager")
    private final AuthenticationManager clientAuthManager;
    @Qualifier("userAuthManager")
    private final AuthenticationManager userAuthManager;
    private final ClientService clientSerivce;
    private final UserService userService;

    public AuthService(
            JwtService jwtservice,
            @Qualifier("clientAuthManager") AuthenticationManager clientAuthManager,
            @Qualifier("userAuthManager") AuthenticationManager userAuthManager, ClientService clientSerivce,
            UserService userService) {
        this.jwtService = jwtservice;
        this.clientAuthManager = clientAuthManager;
        this.userAuthManager = userAuthManager;
        this.clientSerivce = clientSerivce;
        this.userService = userService;
    }

    public String[] validateClientLogin(LoginRequest loginRequest) {
        Authentication authentication = clientAuthManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String accessToken = jwtService.createToken(authUser);
        String refreshToken = jwtService.refreshToken(authUser);
        return new String[] { accessToken, refreshToken, authUser.getUserId().toString(), authUser.getUsername() };
    }

    public String[] validateUserLogin(LoginRequest loginRequest) {
        System.out.println("Inside user validion method");
        Authentication authentication = userAuthManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String accessToken = jwtService.createToken(authUser);
        String refreshToken = jwtService.refreshToken(authUser);
        return new String[] { accessToken, refreshToken, authUser.getUserId().toString(), authUser.getUsername() };
    }

    public String[] refreshToken(String refreshToken) {
        UUID userId = jwtService.generateUserIdFromToken(refreshToken);
        UserType userType = jwtService.extractUserType(refreshToken);
        AuthUser user = null;
        if (UserType.Client == userType) {
            user = clientSerivce.loadUserById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
        } else if (UserType.User == userType) {
            user = userService.loadUserById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        user.setUserType(userType);
        String accessToken = jwtService.createToken(user);
        refreshToken = jwtService.refreshToken(user);
        return new String[] { accessToken, refreshToken, user.getUserId().toString(), user.getUsername() };
    }

    public Map<String, Object> getUserinfo(String token) throws UsernameNotFoundException {
        UUID userId = jwtService.generateUserIdFromToken(token);
        UUID clientId = jwtService.extractClientIDFromToken(token);
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("sub", user.getUserId());
        userInfo.put("name", user.getFirstName() + user.getLastName());
        userInfo.put("email", user.getEmail());
        userInfo.put("client_id",clientId.toString());
        return userInfo;
    }

}
