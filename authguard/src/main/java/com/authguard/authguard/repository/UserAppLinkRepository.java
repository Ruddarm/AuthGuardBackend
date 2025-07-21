package com.authguard.authguard.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.UserAppLinkEntity;
import com.authguard.authguard.model.entity.UserEntity;

public interface UserAppLinkRepository extends JpaRepository<UserAppLinkEntity, UUID> {
    
    Optional<UserAppLinkEntity> findByUserAndApp(UserEntity user, AppEntity app);
    boolean existsByUserAndApp(UserEntity user, AppEntity app);

}
