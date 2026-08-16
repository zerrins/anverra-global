package com.anverraglobal.insurer.contracts;

import java.util.UUID;

public interface InsurerVerificationContract {
    void verifyInsurerActive(UUID insurerId);
}
