package com.authguard.authguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authguard.authguard.model.dto.ClientRequest;
import com.authguard.authguard.model.dto.ClientResponse;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.model.mapper.ClientMapper;
import com.authguard.authguard.services.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;

    // @GetMapping("/login")
    // public ResponseEntity<ClientResponse> getMethodName() {
    // // Resp4onse<Ent
    // return new ResponseEntity<>(new ClientResponse("ruddarm", "123"),
    // HttpStatus.OK);
    // }
    @PostMapping("/signup")
    public ResponseEntity<ClientResponse> singup(@Valid @RequestBody ClientRequest clientRequest) {
        ClientEntity clientEntity = clientService.saveClient(ClientMapper.toClientEntity(clientRequest));
        return new ResponseEntity<>(ClientMapper.toClientResponse(clientEntity), HttpStatus.OK);
    }

}
