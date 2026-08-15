package com.anverraglobal.commission.application.port.outbound;

import com.anverraglobal.commission.domain.Commission;

import java.util.Optional;
import java.util.UUID;

public interface CommissionRepositoryPort {
    
    Optional<Commission> findById(UUID policyId);
    
    Commission save(Commission commission);
}
