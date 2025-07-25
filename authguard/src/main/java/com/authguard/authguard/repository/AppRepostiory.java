package com.authguard.authguard.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.authguard.authguard.model.dto.AppSummary;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.ClientEntity;

public interface AppRepostiory extends JpaRepository<AppEntity, UUID> {

    @Query("SELECT  new com.authguard.authguard.model.dto.AppSummary(app.appId,app.appName) from AppEntity app where app.client.clientId=:clientId")
    public List<AppSummary> findAppSummaryByClientId(@Param("clientId") UUID clientId);

    public Optional<AppEntity> findByAppIdAndClient(UUID appId, ClientEntity client);

}
