package com.authguard.authguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.Exception.ResourceFound;
import com.authguard.authguard.model.dto.ClientRequest;
import com.authguard.authguard.model.dto.ClientResponse;
import com.authguard.authguard.model.dto.LoginRequest;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.model.mapper.ClientMapper;
import com.authguard.authguard.services.ClientAuthService;
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
    private final ClientAuthService clientAuthService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request,
            HttpServletResponse response) {
        String token = clientAuthService.validateLogin(loginRequest);
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }

    @PostMapping("/signup")
    public ResponseEntity<ClientResponse> singup(@Valid @RequestBody ClientRequest clientRequest) throws ResourceFound {
        ClientEntity clientEntity = clientService.saveClient(ClientMapper.toClientEntity(clientRequest));
        return new ResponseEntity<>(ClientMapper.toClientResponse(clientEntity), HttpStatus.OK);
    }
}
