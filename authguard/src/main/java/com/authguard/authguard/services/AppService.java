package com.authguard.authguard.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceFound;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.AppRepostiory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepostiory appRepostiory;
    private final ClientService clientService;

    @Transactional
    public AppEntity createApp(AppEntity appEntity, UUID clientId) throws ResourceFound {
        ClientEntity client = clientService.clientRepo.findById(clientId)
                .orElseThrow(() -> new ResourceFound("Client not found"));
        appEntity.setClient(client);
        client.getApps().add(appEntity);
        return appEntity;
    }
}