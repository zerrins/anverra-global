package com.anverraglobal.commission.adapter.outbound.persistence;

import com.anverraglobal.commission.domain.Commission;
import com.anverraglobal.commission.domain.CommissionStatus;
import com.anverraglobal.commission.domain.CommissionType;
import com.anverraglobal.commission.application.port.outbound.CommissionRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CommissionPersistenceAdapter implements CommissionRepositoryPort {

    private final CommissionRepository commissionRepository;

    public CommissionPersistenceAdapter(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    @Override
    public Optional<Commission> findById(UUID policyId) {
        return commissionRepository.findById(policyId).map(this::mapToDomain);
    }

    @Override
    public Commission save(Commission commission) {
        CommissionEntity entity = new CommissionEntity();
        entity.setPolicyId(commission.getPolicyId());
        entity.setStatus(commission.getStatus().name());
        entity.setType(commission.getType() != null ? commission.getType().name() : null);
        entity.setTotalCommissionValue(commission.getTotalCommissionValue());
        entity.setAgentAShare(commission.getAgentAShare());
        entity.setAgentBShare(commission.getAgentBShare());
        entity.setVersion(commission.getVersion());

        CommissionEntity saved = commissionRepository.save(entity);
        return mapToDomain(saved);
    }

    private Commission mapToDomain(CommissionEntity entity) {
        return new Commission(
                entity.getPolicyId(),
                CommissionStatus.valueOf(entity.getStatus()),
                entity.getType() != null ? CommissionType.valueOf(entity.getType()) : null,
                entity.getTotalCommissionValue(),
                entity.getAgentAShare(),
                entity.getAgentBShare(),
                entity.getVersion()
        );
    }
}
