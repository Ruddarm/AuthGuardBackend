package com.authguard.authguard.model.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCodePayload implements Serializable {
    private UUID userId;
    private UUID client_id;
    private String client_secret;
    private UUID appUserLinkId;
    private String nonce;
}
