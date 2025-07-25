package com.authguard.authguard.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppRequest {
    @NotNull
    @NotBlank
    @Size(min=2)
    private String name;

}
