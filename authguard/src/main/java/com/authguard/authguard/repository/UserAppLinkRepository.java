package com.authguard.authguard.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.UserAppLinkEntity;
import com.authguard.authguard.model.entity.UserEntity;

public interface UserAppLinkRepository extends JpaRepository<UserAppLinkEntity, UUID> {
    
    @Query("select  new com.authguard.authguard.model.dto.UserResponse(link.user.firstName,link.user.lastName,link.user.email) from UserAppLinkEntity link where link.app.appId = :appId ")
    List<UserResponse> findUserByApp(@Param("appId")UUID appId);
    Optional<UserAppLinkEntity> findByUserAndApp(UserEntity user, AppEntity app);
    boolean existsByUserAndApp(UserEntity user, AppEntity app);


}
