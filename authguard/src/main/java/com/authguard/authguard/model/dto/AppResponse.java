package com.authguard.authguard.model.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppResponse {
    private UUID appId;
    private String appName;
    private List<String> domainList;
    private boolean status;
    private UUID apiKey;
}
