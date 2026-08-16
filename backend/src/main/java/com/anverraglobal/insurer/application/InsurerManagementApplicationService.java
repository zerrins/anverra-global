package com.anverraglobal.insurer.application;

import com.anverraglobal.insurer.application.port.outbound.InsurerRepositoryPort;
import com.anverraglobal.insurer.domain.Insurer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class InsurerManagementApplicationService {

    private final InsurerRepositoryPort repository;

    public InsurerManagementApplicationService(InsurerRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Insurer createInsurer(String name) {
        if (repository.findByNameIgnoreCase(name).isPresent()) {
            throw new IllegalArgumentException("Insurer with this name already exists");
        }
        Insurer insurer = Insurer.create(name);
        return repository.save(insurer);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Insurer updateInsurer(UUID id, String name) {
        Insurer insurer = getInsurer(id);
        
        Optional<Insurer> existing = repository.findByNameIgnoreCase(name);
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new IllegalArgumentException("Insurer with this name already exists");
        }

        insurer.update(name);
        return repository.save(insurer);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void activateInsurer(UUID id) {
        Insurer insurer = getInsurer(id);
        insurer.activate();
        repository.save(insurer);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deactivateInsurer(UUID id) {
        Insurer insurer = getInsurer(id);
        insurer.deactivate();
        repository.save(insurer);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Insurer getInsurer(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Insurer not found"));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Page<Insurer> searchInsurers(String name, String status, Pageable pageable) {
        return repository.search(name, status, pageable);
    }
}
