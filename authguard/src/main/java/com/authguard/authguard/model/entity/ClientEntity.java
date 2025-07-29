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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String hashPassword;
    // @Column(unique = true, nullable = false)
    // private String contactNumber;
    @Column
    @CreationTimestamp
    private LocalDate createdAt;
    @Column(unique = true)
    private String country;
    @Column
    private boolean emailVerified;
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<AppEntity> apps;

}
