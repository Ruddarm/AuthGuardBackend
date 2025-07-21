package com.authguard.authguard.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_app")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAppLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID linkID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_Id", nullable = false)
    private AppEntity app;

    @CreationTimestamp
    private LocalDateTime firstLogin;
    private LocalDateTime lastLogin;

}
