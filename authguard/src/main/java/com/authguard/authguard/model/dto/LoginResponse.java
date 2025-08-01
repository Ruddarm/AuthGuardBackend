package com.authguard.authguard.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String userId;
    private String userEmail;
    private String accessToken;
}
