package com.anverraglobal.commission.adapter.outbound.persistence;

import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface CommissionRepository extends CrudRepository<CommissionEntity, UUID> {
}
