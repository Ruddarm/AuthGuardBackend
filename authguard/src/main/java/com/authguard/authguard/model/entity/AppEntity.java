package com.authguard.authguard.model.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID appId;
    @Column(nullable=false)
    private String appName;
    @Column
    private boolean status;
    @CreationTimestamp
    private LocalDate createdAt;
    @ManyToOne
    @JoinColumn(nullable = false, name = "clientId")
    private ClientEntity client;
    @OneToOne
    @JoinColumn(name = "apiKeyId")
    private ApiKeyEntity apiKeyEntity;
    @OneToMany(mappedBy = "app", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE} , orphanRemoval=true)
    private List<UserEntity> user;
}
