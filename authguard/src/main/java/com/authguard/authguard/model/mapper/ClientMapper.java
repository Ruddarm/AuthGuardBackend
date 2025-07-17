package com.authguard.authguard.model.mapper;

import com.authguard.authguard.model.dto.ClientRequest;
import com.authguard.authguard.model.dto.ClientResponse;
import com.authguard.authguard.model.entity.ClientEntity;

public class ClientMapper {

    public static ClientEntity toClientEntity(ClientRequest clientRequest) {
        ClientEntity client = ClientEntity.builder().name(clientRequest.getName()).email(clientRequest.getEmail())
                .contactNumber(clientRequest.getContactNumber()).country(clientRequest.getCountry())
                .hashPassword(clientRequest.getPassword())
                .build();

        return client;
    }

    public static ClientResponse toClientResponse(ClientEntity clientEntity) {
        ClientResponse client = ClientResponse.builder().clientId(clientEntity.getClientId())
                .name(clientEntity.getName()).email(clientEntity.getEmail()).build();
        return client;
    }
}
