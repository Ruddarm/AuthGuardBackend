package com.authguard.authguard.services;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.ClientAuth;
import com.authguard.authguard.model.dto.LoginRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClientService clientSerivce;

    public String[] validateLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        ClientAuth clientUser = (ClientAuth) authentication.getPrincipal();
        String accessToken = jwtService.createToken(clientUser);
        String refreshToken = jwtService.refreshToken(clientUser);
        return new String[] { accessToken, refreshToken, clientUser.getUserId().toString() };
    }

    public String[] refreshToken(String refreshToken) {
        UUID clientId = jwtService.generateUserIdFromToken(refreshToken);
        ClientAuth user = clientSerivce.loadUserById(clientId)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found"));

        String accessToken = jwtService.createToken(user);
        refreshToken = jwtService.refreshToken(user);
        return new String[] { accessToken, refreshToken };
    }


}
