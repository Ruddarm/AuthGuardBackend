package com.authguard.authguard.services;

import java.util.List;
import java.util.UUID;
import java.util.jar.Attributes;

import org.hibernate.sql.Update;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.AppSummary;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.AppRepostiory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppService {
    private final ClientService clientService;
    private final AppRepostiory appRepository;

    @Transactional
    public AppEntity createApp(AppEntity appEntity, UUID userId) throws ResourceException {
        ClientEntity client = clientService.findById(userId)
                .orElseThrow(() -> new ResourceException("Client not found"));
        appEntity.setClient(client);
        // ApiKeyEntity apiKeyEntity = new ApiKeyEntity();
        // apiKeyEntity.setApiKey(UUID.randomUUID());
        appEntity.setClient_secret(UUID.randomUUID().toString());
        client.getApps().add(appEntity);
        return appEntity;
    }

    @Cacheable(cacheNames = "validatedapps", key = "#client_id")
    public AppEntity validateApp(UUID client_id) throws ResourceException {
        // ClientEntity client = clientService.clientRepo.findById(clientId)
        // .orElseThrow(() -> new ResourceException("Client Not Found"));
        AppEntity app = appRepository.findById(client_id)
                .orElseThrow(() -> new ResourceException("App not found with this Client Id"));
        // log.info("validated app is called");
        return app;
    }

    @CachePut(cacheNames = "appListSummary", key = "#userId")
    public List<AppSummary> UpdateApplistCache(UUID userId) throws ResourceException {
        try {
            return appRepository.findAppSummaryByUserId(userId);
        } catch (Exception ex) {
            throw new ResourceException(ex.getMessage());
        }
    }

    @Cacheable(cacheNames = "appListSummary", key = "#userId")
    public List<AppSummary> getAppSummary(String userId) throws ResourceException {
        try {
            return appRepository.findAppSummaryByUserId(UUID.fromString(userId));
        } catch (Exception ex) {
            throw new ResourceException(ex.getMessage());
        }
    }

    // @Transactional
    // public String generateApiKey(UUID appID) throws ResourceException {
    // AppEntity app = appRepository.findById(appID)
    // .orElseThrow(() -> new ResourceException("Invalid app ID"));

    // UUID newKey = UUID.randomUUID();
    // ApiKeyEntity apiKey = new ApiKeyEntity();
    // apiKey.setApiKey(newKey);
    // app.setApiKeyEntity(apiKey);
    // appRepository.save(app); //
    // return newKey.toString();
    // }

    public void deleteApp(UUID client_id) {
        appRepository.deleteById(client_id);
    }

}