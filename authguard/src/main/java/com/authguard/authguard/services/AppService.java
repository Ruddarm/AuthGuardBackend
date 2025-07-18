package com.authguard.authguard.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.ClientEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppService {
    private final ClientService clientService;

    @Transactional
    public AppEntity createApp(AppEntity appEntity, UUID clientId) throws ResourceException {
        ClientEntity client = clientService.clientRepo.findById(clientId)
                .orElseThrow(() -> new ResourceException("Client not found"));
        appEntity.setClient(client);
        client.getApps().add(appEntity);
        return appEntity;
    }
}