package com.authguard.authguard.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
            if (!userAppLinkRepo.existsByUserAndApp(user, app)) {
                UserAppLinkEntity link = UserAppLinkEntity.builder()
                        .user(user)
                        .app(app)
                        .lastLogin(LocalDateTime.now())
                        .build();
                userAppLinkRepo.save(link);
            }

        }
    }

}
