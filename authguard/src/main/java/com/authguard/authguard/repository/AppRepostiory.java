package com.authguard.authguard.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.ClientEntity;

public interface AppRepostiory extends JpaRepository<AppEntity, UUID> {

    public Optional<AppEntity> findByAppIdAndClient(UUID appId, ClientEntity client);
    
}
