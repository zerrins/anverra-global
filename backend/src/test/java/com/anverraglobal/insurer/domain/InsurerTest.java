package com.anverraglobal.insurer.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InsurerTest {

    @Test
    void shouldCreateInsurer() {
        Insurer insurer = Insurer.create("Global Health");
        assertThat(insurer.getId()).isNotNull();
        assertThat(insurer.getName()).isEqualTo("Global Health");
        assertThat(insurer.getStatus()).isEqualTo(InsurerStatus.ACTIVE);
        assertThat(insurer.getCreatedAt()).isNotNull();
        assertThat(insurer.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldRejectBlankNameOnCreate() {
        assertThatThrownBy(() -> Insurer.create(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldUpdateName() {
        Insurer insurer = Insurer.create("Global Health");
        insurer.update("Global Health Inc");
        assertThat(insurer.getName()).isEqualTo("Global Health Inc");
    }

    @Test
    void shouldDeactivate() {
        Insurer insurer = Insurer.create("Global Health");
        insurer.deactivate();
        assertThat(insurer.getStatus()).isEqualTo(InsurerStatus.INACTIVE);
    }

    @Test
    void shouldActivate() {
        Insurer insurer = Insurer.create("Global Health");
        insurer.deactivate();
        insurer.activate();
        assertThat(insurer.getStatus()).isEqualTo(InsurerStatus.ACTIVE);
    }
}
