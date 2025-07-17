package com.authguard.authguard.model.entity;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "client")
@Data
@Builder
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID clientId;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String hashPassword;
    @Column(unique = true, nullable = false)
    private String contactNumber;
    @Column
    @CreationTimestamp
    private LocalDate createdAt;
    @Column(unique = true)
    private String country;
    @Column
    private boolean emailVerified;
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY , orphanRemoval = true)
    private List<AppEntity> apps;

}
