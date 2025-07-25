package com.authguard.authguard.services;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.AuthUser;
import com.authguard.authguard.model.domain.ClientAuth;
import com.authguard.authguard.model.domain.UserType;
import com.authguard.authguard.model.dto.LoginRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClientService clientSerivce;
    private final UserService userService;

    public String[] validateLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String accessToken = jwtService.createToken(authUser);
        String refreshToken = jwtService.refreshToken(authUser);
        return new String[] { accessToken, refreshToken, authUser.getUserId().toString() };
    }

    public String[] refreshToken(String refreshToken) {
        UUID clientId = jwtService.generateUserIdFromToken(refreshToken);
        UserType userType = jwtService.extractUserType(refreshToken);
        AuthUser user = null;
        if (UserType.Client == userType) {
            user = clientSerivce.loadUserById(clientId)
                    .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
        } else if (UserType.User == userType) {
            user = userService.loadUserById(clientId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        user.setUserType(userType);
        String accessToken = jwtService.createToken(user);
        refreshToken = jwtService.refreshToken(user);
        return new String[] { accessToken, refreshToken };
    }

}
