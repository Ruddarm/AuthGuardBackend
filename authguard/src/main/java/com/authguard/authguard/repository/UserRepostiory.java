package com.authguard.authguard.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authguard.authguard.model.entity.UserEntity;

public interface UserRepostiory extends JpaRepository<UserEntity, UUID> {
    public boolean existsByEmail(String email);

    public boolean existsByContactNumber(String number);

    public Optional<UserEntity> findByEmail(String email);
}