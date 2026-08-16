package com.anverraglobal.insurer.application.port.outbound;

import com.anverraglobal.insurer.domain.Insurer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface InsurerRepositoryPort {
    Insurer save(Insurer insurer);
    Optional<Insurer> findById(UUID id);
    Optional<Insurer> findByNameIgnoreCase(String name);
    Page<Insurer> search(String name, String status, Pageable pageable);
}
