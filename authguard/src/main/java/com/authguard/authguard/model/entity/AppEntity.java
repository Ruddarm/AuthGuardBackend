package com.authguard.authguard.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
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
public class AppEntity implements  Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID client_id;
    @Column(nullable = false)
    private String appName;
    @Column
    private boolean status;
    @CreationTimestamp
    private LocalDate createdAt;
    @ManyToOne
    @JoinColumn(nullable = false, name = "userId")
    private ClientEntity client;
    @Column
    private String client_secret;
    // @OneToOne(cascade = { CascadeType.ALL })
    // @JoinColumn(name = "apiKeyId")
    // private ApiKeyEntity apiKeyEntity;

    @OneToMany(mappedBy = "app", cascade = { CascadeType.ALL }, orphanRemoval = true , fetch=FetchType.LAZY)
    private Set<UserAppLinkEntity> appUsers;
}
