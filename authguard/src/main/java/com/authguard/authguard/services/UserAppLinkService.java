package com.authguard.authguard.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.model.entity.AppEntity;
import com.authguard.authguard.model.entity.UserAppLinkEntity;
import com.authguard.authguard.model.entity.UserEntity;
import com.authguard.authguard.repository.UserAppLinkRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAppLinkService {
    private final UserAppLinkRepository userAppLinkRepo;

    @Transactional
    public void linkUserApp(AppEntity app, UserEntity user) {
        Optional<UserAppLinkEntity> userapplink = userAppLinkRepo.findByUserAndApp(user, app);
        if (userapplink.isEmpty()) {
                UserAppLinkEntity link = UserAppLinkEntity.builder()
                        .user(user)
                        .app(app)
                        .lastLogin(LocalDateTime.now())
                        .build();
                userAppLinkRepo.save(link);
        }
    }

    public UserAppLinkEntity getApplink(AppEntity app, UserEntity user) {
        return userAppLinkRepo.findByUserAndApp(user, app).orElse(null);
    }

    public List<UserResponse> getListOfUserByApp(UUID appId) {
        return userAppLinkRepo.findUserByApp(appId);
    }

}
