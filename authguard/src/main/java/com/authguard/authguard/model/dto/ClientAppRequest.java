package com.authguard.authguard.model.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientAppRequest {
    @NotNull
    private UUID client_id;
    @NotNull
    @NotBlank
    private String redirectUrl;
    private String nonce;
}
