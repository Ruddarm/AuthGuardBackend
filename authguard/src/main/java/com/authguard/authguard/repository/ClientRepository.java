package com.authguard.authguard.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authguard.authguard.model.entity.ClientEntity;

public  interface   ClientRepository extends  JpaRepository<ClientEntity, UUID> {
    public boolean existsByEmail(String email);
    public boolean existsByContactNumber(String number);
    public Optional<ClientEntity> findByEmail(String email);
    
}