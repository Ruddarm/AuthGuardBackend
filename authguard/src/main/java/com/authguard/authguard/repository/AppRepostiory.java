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

    @Query("SELECT  new com.authguard.authguard.model.dto.AppSummary(app.client_id,app.appName) from AppEntity app where app.client.userId=:userId")
    public List<AppSummary> findAppSummaryByUserId(@Param("userId") UUID userId);

    // public Optional<AppEntity> findByClient_idAndClient(UUID client_id, ClientEntity client);

}
