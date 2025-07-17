package com.authguard.authguard.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.authguard.authguard.model.entity.AppEntity;

public interface AppRepostiory extends JpaRepository<AppEntity, UUID> {

}
