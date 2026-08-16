package com.anverraglobal.insurer.application;

import com.anverraglobal.insurer.domain.Insurer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsurerVerificationServiceImplTest {

    @Mock
    private InsurerManagementApplicationService service;

    private InsurerVerificationServiceImpl verificationService;

    @BeforeEach
    void setUp() {
        verificationService = new InsurerVerificationServiceImpl(service);
    }

    @Test
    void shouldPassIfActive() {
        Insurer insurer = Insurer.create("Test");
        when(service.getInsurer(insurer.getId())).thenReturn(insurer);
        verificationService.verifyInsurerActive(insurer.getId());
    }

    @Test
    void shouldThrowIfInactive() {
        Insurer insurer = Insurer.create("Test");
        insurer.deactivate();
        when(service.getInsurer(insurer.getId())).thenReturn(insurer);
        
        assertThatThrownBy(() -> verificationService.verifyInsurerActive(insurer.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIfNotFound() {
        Insurer insurer = Insurer.create("Test");
        when(service.getInsurer(insurer.getId())).thenThrow(new NoSuchElementException("Not found"));
        
        assertThatThrownBy(() -> verificationService.verifyInsurerActive(insurer.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
