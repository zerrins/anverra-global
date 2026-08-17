package com.anverraglobal.organization.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrganizationDomainTest {

    @Test
    void dealerLifecycle() {
        Dealer dealer = Dealer.create("Test Dealer");
        assertThat(dealer.getName()).isEqualTo("Test Dealer");
        assertThat(dealer.getStatus()).isEqualTo(OrganizationStatus.ACTIVE);

        dealer.deactivate();
        assertThat(dealer.getStatus()).isEqualTo(OrganizationStatus.INACTIVE);

        assertThatThrownBy(dealer::deactivate)
                .isInstanceOf(IllegalStateException.class);

        dealer.activate();
        assertThat(dealer.getStatus()).isEqualTo(OrganizationStatus.ACTIVE);

        assertThatThrownBy(dealer::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void branchLifecycle() {
        UUID dealerId = UUID.randomUUID();
        Branch branch = Branch.create(dealerId, "Test Branch");
        assertThat(branch.getName()).isEqualTo("Test Branch");
        assertThat(branch.getDealerId()).isEqualTo(dealerId);
        assertThat(branch.getStatus()).isEqualTo(OrganizationStatus.ACTIVE);

        branch.deactivate();
        assertThat(branch.getStatus()).isEqualTo(OrganizationStatus.INACTIVE);

        assertThatThrownBy(branch::deactivate)
                .isInstanceOf(IllegalStateException.class);

        branch.activate();
        assertThat(branch.getStatus()).isEqualTo(OrganizationStatus.ACTIVE);

        assertThatThrownBy(branch::activate)
                .isInstanceOf(IllegalStateException.class);
    }
}
