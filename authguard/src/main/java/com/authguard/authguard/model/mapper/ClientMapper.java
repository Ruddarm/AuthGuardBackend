package com.authguard.authguard.model.mapper;

import com.authguard.authguard.model.dto.ClientRequest;
import com.authguard.authguard.model.dto.ClientResponse;
import com.authguard.authguard.model.entity.ClientEntity;

public class ClientMapper {

    public static ClientEntity toClientEntity(ClientRequest clientRequest) {
        ClientEntity client = ClientEntity.builder().firstName(clientRequest.getFirstName())
                .lastName(clientRequest.getLastName()).email(clientRequest.getEmail())
                                .hashPassword(clientRequest.getPassword())
                .build();

        return client;
    }
    
    public static ClientResponse toClientResponse(ClientEntity clientEntity) {
        ClientResponse client = ClientResponse.builder().userId(clientEntity.getUserId())
                .firstName(clientEntity.getFirstName()).lastName(clientEntity.getLastName())
                .email(clientEntity.getEmail()).build();
        return client;
    }
}
