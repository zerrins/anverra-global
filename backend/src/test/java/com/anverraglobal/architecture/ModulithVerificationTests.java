package com.anverraglobal.architecture;

import com.anverraglobal.AnverraGlobalApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithVerificationTests {

    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(AnverraGlobalApplication.class);
        modules.verify();
    }
}
