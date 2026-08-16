package com.anverraglobal.insurer.adapter.outbound.persistence;

import com.anverraglobal.insurer.application.port.outbound.InsurerRepositoryPort;
import com.anverraglobal.insurer.domain.Insurer;
import com.anverraglobal.insurer.domain.InsurerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InsurerPersistenceAdapter implements InsurerRepositoryPort {

    private final InsurerRepository repository;

    public InsurerPersistenceAdapter(InsurerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Insurer save(Insurer insurer) {
        InsurerEntity entity = toEntity(insurer);
        InsurerEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Insurer> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Insurer> findByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name).map(this::toDomain);
    }

    @Override
    public Page<Insurer> search(String name, String status, Pageable pageable) {
        if (name != null && !name.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
            return repository.findByNameContainingIgnoreCaseAndStatus(name, status, pageable).map(this::toDomain);
        } else if (name != null && !name.trim().isEmpty()) {
            return repository.findByNameContainingIgnoreCase(name, pageable).map(this::toDomain);
        } else if (status != null && !status.trim().isEmpty()) {
            return repository.findByStatus(status, pageable).map(this::toDomain);
        } else {
            return repository.findAll(pageable).map(this::toDomain);
        }
    }

    private InsurerEntity toEntity(Insurer domain) {
        return new InsurerEntity(
                domain.getId(),
                domain.getName(),
                domain.getStatus().name(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getVersion()
        );
    }

    private Insurer toDomain(InsurerEntity entity) {
        return new Insurer(
                entity.getId(),
                entity.getName(),
                InsurerStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getVersion()
        );
    }
}
