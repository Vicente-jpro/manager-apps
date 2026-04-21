package com.manager.apps.repository;

import com.manager.apps.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    Optional<Client> findByEmail(String email);

    @Query("UPDATE Client c SET c.usedDay = c.usedDay + 1 WHERE c.status = 'active' AND c.usedDay < c.maxDay")
    @Modifying
    @Transactional
    int incrementUsedDay();

    @Query("SELECT c FROM Client c WHERE c.status = 'active' AND (c.maxDay - c.usedDay) = 5")
    List<Client> findClientsNearLimit();
}
