package com.anverraglobal.insurer.application;

import com.anverraglobal.insurer.contracts.InsurerVerificationContract;
import com.anverraglobal.insurer.domain.Insurer;
import com.anverraglobal.insurer.domain.InsurerStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class InsurerVerificationServiceImpl implements InsurerVerificationContract {

    private final InsurerManagementApplicationService service;

    public InsurerVerificationServiceImpl(InsurerManagementApplicationService service) {
        this.service = service;
    }

    @Override
    @Transactional(readOnly = true)
    public void verifyInsurerActive(UUID insurerId) {
        try {
            Insurer insurer = service.getInsurer(insurerId);
            if (insurer.getStatus() != InsurerStatus.ACTIVE) {
                throw new IllegalStateException("Insurer is inactive");
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Insurer not found");
        }
    }
}
