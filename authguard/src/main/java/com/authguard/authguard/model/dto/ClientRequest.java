package com.authguard.authguard.model.dto;

import com.authguard.authguard.model.baseModel.BaseClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ClientRequest extends  BaseClient {
    String hashPasword;

}
