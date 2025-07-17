package com.authguard.authguard.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="apikeys")
public class ApiKeyEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String apiKeyId;
    @Column(nullable=true,unique=true)
    private String apiKey;

    @OneToOne(mappedBy="apiKeyEntity")
    private AppEntity app;
}
