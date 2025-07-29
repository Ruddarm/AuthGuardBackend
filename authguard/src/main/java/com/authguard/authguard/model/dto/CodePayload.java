package com.authguard.authguard.model.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodePayload {
    @NotNull(message = "Auth code is required")
    @NotBlank(message = "Auth code is blank")
    private String code;
    @NotNull(message = "client id is null")
    private UUID client_id;
    @NotNull(message = "apikey is null")
    private String client_secret;
}
