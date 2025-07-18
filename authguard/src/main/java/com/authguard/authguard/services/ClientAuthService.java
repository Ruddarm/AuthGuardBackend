package com.authguard.authguard.services;
import org.springframework.security.core.Authentication;
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

    public String validateLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        ClientUser clientUser = (ClientUser) authentication.getPrincipal();
        String token = jwtService.createToken(clientUser);
        return token;
    }

}
