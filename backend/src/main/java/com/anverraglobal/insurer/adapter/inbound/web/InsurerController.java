package com.anverraglobal.insurer.adapter.inbound.web;

import com.anverraglobal.insurer.application.InsurerManagementApplicationService;
import com.anverraglobal.insurer.domain.Insurer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/insurers")
public class InsurerController {

    private final InsurerManagementApplicationService service;

    public InsurerController(InsurerManagementApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InsurerResponse> createInsurer(@RequestBody CreateInsurerRequest request) {
        Insurer insurer = service.createInsurer(request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(insurer));
    }

    @GetMapping
    public ResponseEntity<Page<InsurerResponse>> listInsurers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        Page<Insurer> insurers = service.searchInsurers(name, status, pageable);
        return ResponseEntity.ok(insurers.map(this::mapToResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurerResponse> getInsurer(@PathVariable UUID id) {
        Insurer insurer = service.getInsurer(id);
        return ResponseEntity.ok(mapToResponse(insurer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurerResponse> updateInsurer(
            @PathVariable UUID id,
            @RequestBody UpdateInsurerRequest request) {
        Insurer insurer = service.updateInsurer(id, request.name());
        return ResponseEntity.ok(mapToResponse(insurer));
    }

    @PostMapping("/{id}/lifecycle/activate")
    public ResponseEntity<Void> activateInsurer(@PathVariable UUID id) {
        service.activateInsurer(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/lifecycle/deactivate")
    public ResponseEntity<Void> deactivateInsurer(@PathVariable UUID id) {
        service.deactivateInsurer(id);
        return ResponseEntity.ok().build();
    }

    private InsurerResponse mapToResponse(Insurer insurer) {
        return new InsurerResponse(
                insurer.getId(),
                insurer.getName(),
                insurer.getStatus().name(),
                insurer.getCreatedAt(),
                insurer.getUpdatedAt(),
                insurer.getVersion()
        );
    }

    public record CreateInsurerRequest(String name) {}
    public record UpdateInsurerRequest(String name) {}
    public record InsurerResponse(UUID id, String name, String status, Instant createdAt, Instant updatedAt, Long version) {}
}
