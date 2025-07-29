package com.authguard.authguard.model.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse  {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String accessToken;
}
