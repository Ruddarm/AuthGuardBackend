package com.authguard.authguard.model.dto;

import java.util.UUID;

import com.authguard.authguard.model.baseModel.BaseClient;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ClientResponse extends BaseClient {
    private UUID clientId;

}
