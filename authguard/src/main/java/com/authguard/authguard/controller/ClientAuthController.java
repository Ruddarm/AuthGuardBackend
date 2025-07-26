package com.authguard.authguard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.ClientRequest;
import com.authguard.authguard.model.dto.ClientResponse;
import com.authguard.authguard.model.dto.LoginRequest;
import com.authguard.authguard.model.dto.LoginResponse;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.model.mapper.ClientMapper;
import com.authguard.authguard.services.AuthService;
import com.authguard.authguard.services.ClientService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/client")
@RequiredArgsConstructor
public class ClientAuthController {
    private final ClientService clientService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        String token[] = authService.validateClientLogin(loginRequest);
        // Cookie refreshToken = new Cookie("refresh_token", token[1]);
        // // refreshToken.setHttpOnly(true);
        // refreshToken.setPath("/");
        // refreshToken.setSecure(false);
        // refreshToken.setDomain("localhost");
        // response.addCookie(refreshToken);

        String cookie = String.format(
                "client_refresh_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                token[1], 7 * 24 * 60 * 60);
        response.setHeader("Set-Cookie", cookie);

        return new ResponseEntity<>(LoginResponse.builder().accessToken(token[0]).clientID(token[2]).build(),
                HttpStatus.ACCEPTED);
    }

    @PostMapping("/signup")
    public ResponseEntity<ClientResponse> singup(@Valid @RequestBody ClientRequest clientRequest)
            throws ResourceException {
        ClientEntity clientEntity = clientService.saveClient(ClientMapper.toClientEntity(clientRequest));
        return new ResponseEntity<>(ClientMapper.toClientResponse(clientEntity), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request,
            HttpServletResponse response) throws ResourceException {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("client_refresh_token")) {
                refreshToken = cookie.getValue();
                break;
            }
        }
        if (refreshToken == null)
            throw new ResourceException("refresh token not found");
        System.out.println(refreshToken);
        String[] tokens = authService.refreshToken(refreshToken);
        // Cookie NewrefreshToken = new Cookie("refresh-token", tokens[1]);
        // NewrefreshToken.setHttpOnly(true);
        // NewrefreshToken.setPath("/auth/client/refresh");
        // response.addCookie(NewrefreshToken);
        String cookie = String.format(
                "client_refresh_token=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=%d",
                tokens[1], 7 * 24 * 60 * 60);
        response.setHeader("Set-Cookie", cookie);
        return new ResponseEntity<>(LoginResponse.builder().accessToken(tokens[0]).build(), HttpStatus.ACCEPTED);
        // return new ResponseEntity<LoginResponse>();
    }

}
