package com.anverraglobal.insurer.application;

import com.anverraglobal.insurer.application.port.outbound.InsurerRepositoryPort;
import com.anverraglobal.insurer.domain.Insurer;
import com.anverraglobal.insurer.domain.InsurerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsurerManagementApplicationServiceTest {

    @Mock
    private InsurerRepositoryPort repositoryPort;

    private InsurerManagementApplicationService service;

    @BeforeEach
    void setUp() {
        service = new InsurerManagementApplicationService(repositoryPort);
    }

    @Test
    void shouldCreateInsurer() {
        when(repositoryPort.findByNameIgnoreCase("Global Health")).thenReturn(Optional.empty());
        when(repositoryPort.save(any(Insurer.class))).thenAnswer(i -> i.getArguments()[0]);

        Insurer insurer = service.createInsurer("Global Health");

        assertThat(insurer.getName()).isEqualTo("Global Health");
        verify(repositoryPort).save(any(Insurer.class));
    }

    @Test
    void shouldRejectDuplicateNameOnCreate() {
        Insurer existing = Insurer.create("Global Health");
        when(repositoryPort.findByNameIgnoreCase("Global Health")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.createInsurer("Global Health"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldActivateInsurer() {
        Insurer insurer = Insurer.create("Global Health");
        insurer.deactivate();
        when(repositoryPort.findById(insurer.getId())).thenReturn(Optional.of(insurer));
        when(repositoryPort.save(any(Insurer.class))).thenAnswer(i -> i.getArguments()[0]);

        service.activateInsurer(insurer.getId());

        assertThat(insurer.getStatus()).isEqualTo(InsurerStatus.ACTIVE);
    }
}
