package com.garden.api.clients;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findAllByStatus(ClientStatus status, Pageable pageable);

    long countByCreatedAtBetween(Instant start, Instant end);

}
