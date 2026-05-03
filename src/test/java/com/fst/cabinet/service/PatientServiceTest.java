package com.fst.cabinet.service;

import com.fst.cabinet.dto.PatientDto;
import com.fst.cabinet.entity.Patient;
import com.fst.cabinet.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock PatientRepository patientRepository;
    @InjectMocks PatientService patientService;

    @Test
    void save_shouldSavePatient() {
        PatientDto dto = PatientDto.builder()
                .cin("12345678").nom("Hammami").prenom("Mohamed")
                .dateNaissance(LocalDate.of(1980, 5, 15))
                .build();

        when(patientRepository.existsByCin("12345678")).thenReturn(false);
        when(patientRepository.save(any())).thenAnswer(inv -> {
            Patient p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        Patient saved = patientService.save(dto);

        assertThat(saved.getCin()).isEqualTo("12345678");
        assertThat(saved.getNom()).isEqualTo("Hammami");
        verify(patientRepository).save(any());
    }

    @Test
    void save_shouldThrowWhenCinDuplicate() {
        PatientDto dto = PatientDto.builder()
                .cin("12345678").nom("X").prenom("Y")
                .dateNaissance(LocalDate.now()).build();

        when(patientRepository.existsByCin("12345678")).thenReturn(true);

        assertThatThrownBy(() -> patientService.save(dto))
                .isInstanceOf(IllegalStateException.class);

        verify(patientRepository, never()).save(any());
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("introuvable");
    }
}
