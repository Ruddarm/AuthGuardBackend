package com.authguard.authguard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/user")
@RestController
public class UserController {
    
    @PostMapping("/get")
    public String postMethodName() {  
        
        return "Name is Fuck Man";
    }
    
}
