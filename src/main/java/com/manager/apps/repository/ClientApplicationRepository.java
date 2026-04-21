package com.manager.apps.repository;

import com.manager.apps.entity.ClientApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientApplicationRepository extends JpaRepository<ClientApplication, UUID> {
    List<ClientApplication> findByClientIdClient(UUID clientId);
    Optional<ClientApplication> findByClientIdClientAndApplicationIdApp(UUID clientId, UUID appId);
    void deleteByClientIdClientAndApplicationIdApp(UUID clientId, UUID appId);
}
