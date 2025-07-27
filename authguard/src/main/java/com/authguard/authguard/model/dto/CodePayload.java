package com.authguard.authguard.model.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodePayload {
    @NotNull(message = "Auth code is required")
    @NotBlank(message = "Auth code is blank")
    private String code;
    @NotNull(message = "client id is null")
    private UUID clientId;
    @NotNull(message = "app id is null")
    private UUID appId;
    @NotNull(message = "apikey is null")
    private UUID apiKey;
}
