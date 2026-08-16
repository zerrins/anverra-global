package com.anverraglobal.insurer.adapter.outbound.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface InsurerRepository extends CrudRepository<InsurerEntity, UUID>, PagingAndSortingRepository<InsurerEntity, UUID> {
    Optional<InsurerEntity> findByNameIgnoreCase(String name);
    Page<InsurerEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<InsurerEntity> findByStatus(String status, Pageable pageable);
    Page<InsurerEntity> findByNameContainingIgnoreCaseAndStatus(String name, String status, Pageable pageable);
}
