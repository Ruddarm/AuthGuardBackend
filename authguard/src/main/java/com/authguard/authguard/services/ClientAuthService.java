package com.authguard.authguard.services;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.ClientUser;
import com.authguard.authguard.model.dto.LoginRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientAuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClientUserService clientUserService;

    public String[] validateLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        ClientUser clientUser = (ClientUser) authentication.getPrincipal();
        String accessToken = jwtService.createToken(clientUser);
        String refreshToken = jwtService.refreshToken(clientUser);
        return new String[] { accessToken, refreshToken };
    }

    public String[] refreshToken(String refreshToken) {
        UUID clientId = jwtService.generateUserIdFromToken(refreshToken);
        ClientUser user = clientUserService.loadUserById(clientId)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found"));

        String accessToken = jwtService.createToken(user);
        refreshToken = jwtService.refreshToken(user);
        return new String[] { accessToken, refreshToken };
    }

}
