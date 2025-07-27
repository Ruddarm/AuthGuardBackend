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
    private UUID clientId;
    private UUID appId;
    private UUID appApiKey;
    private UUID appUserLinkId;
}
