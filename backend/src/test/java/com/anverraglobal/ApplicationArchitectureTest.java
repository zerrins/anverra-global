package com.anverraglobal;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ApplicationArchitectureTest {

    @Test
    void verifyModulithStructure() {
        ApplicationModules modules = ApplicationModules.of("com.anverraglobal");
        modules.verify();
    }
}
