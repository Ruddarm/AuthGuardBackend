package com.authguard.authguard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/client")
public class ClientController {

    @GetMapping("/profile")
    public ResponseEntity<String> getClient() {
        return new ResponseEntity<>("Name is fukman", HttpStatus.OK);
    }

    
}
