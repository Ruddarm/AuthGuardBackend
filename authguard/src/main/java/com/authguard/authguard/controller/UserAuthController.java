package com.authguard.authguard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.UserRequest;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.model.entity.UserEntity;
import com.authguard.authguard.model.mapper.UserMapper;
import com.authguard.authguard.services.UserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserAuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> userSignup(@Valid @RequestBody UserRequest userRequest)
            throws ResourceException {
        System.out.println("fucking here");
        UserResponse userResponse = userService.saveUser(userMapper.userRequestToUserEntity(userRequest));
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody String entity) {
                
        return entity;
    }
    
}
