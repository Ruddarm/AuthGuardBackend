package com.authguard.authguard.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.entity.ApiKeyEntity;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.AppRepostiory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppService {
    private final ClientService clientService;
    private final AppRepostiory appRepository;

    @Transactional
    public AppEntity createApp(AppEntity appEntity, UUID clientId) throws ResourceException {
        ClientEntity client = clientService.clientRepo.findById(clientId)
                .orElseThrow(() -> new ResourceException("Client not found"));
        appEntity.setClient(client);
        client.getApps().add(appEntity);
        return appEntity;
    }

    public AppEntity validateApp(UUID appId, UUID clientId) throws ResourceException {
        ClientEntity client = clientService.clientRepo.findById(clientId)
                .orElseThrow(() -> new ResourceException("Client Not Found"));
        AppEntity app = appRepository.findByAppIdAndClient(appId, client)
                .orElseThrow(() -> new ResourceException("App not found with this Client Id"));
        return app;
    }

    @Transactional
    public String generateApiKey(UUID appID) throws ResourceException {
        AppEntity app = appRepository.findById(appID)
                .orElseThrow(() -> new ResourceException("Invalid app ID"));

        UUID newKey = UUID.randomUUID();
        ApiKeyEntity apiKey = new ApiKeyEntity();
        apiKey.setApiKey(newKey);
        app.setApiKeyEntity(apiKey);
        appRepository.save(app); // 
        return newKey.toString();
    }

}