package com.fst.cabinet.service;

import com.fst.cabinet.dto.RendezVousDto;
import com.fst.cabinet.entity.*;
import com.fst.cabinet.repository.RendezVousRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RendezVousServiceTest {

    @Mock RendezVousRepository rendezVousRepository;
    @Mock PatientService patientService;
    @Mock MedecinService medecinService;

    @InjectMocks RendezVousService rendezVousService;

    private Patient patient;
    private Medecin medecin;

    @BeforeEach
    void setUp() {
        patient = Patient.builder().id(1L).cin("12345678").nom("Hammami").prenom("Mohamed").build();
        medecin = Medecin.builder().id(1L).nom("Ben Ali").prenom("Sami").specialite("Cardiologie").build();
    }

    @Test
    void save_shouldSaveWhenNoChevauchement() {
        RendezVousDto dto = new RendezVousDto();
        dto.setPatientId(1L);
        dto.setMedecinId(1L);
        dto.setDateHeure(LocalDateTime.of(2026, 6, 1, 9, 0));
        dto.setDureeMinutes(30);

        when(rendezVousRepository.findChevauchements(eq(1L), any(), any(), isNull())).thenReturn(List.of());
        when(patientService.findById(1L)).thenReturn(patient);
        when(medecinService.findById(1L)).thenReturn(medecin);
        when(rendezVousRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RendezVous saved = rendezVousService.save(dto);

        assertThat(saved.getPatient()).isEqualTo(patient);
        assertThat(saved.getMedecin()).isEqualTo(medecin);
        assertThat(saved.getDureeMinutes()).isEqualTo(30);
        verify(rendezVousRepository).save(any());
    }

    @Test
    void save_shouldThrowWhenChevauchement() {
        RendezVousDto dto = new RendezVousDto();
        dto.setPatientId(1L);
        dto.setMedecinId(1L);
        dto.setDateHeure(LocalDateTime.of(2026, 6, 1, 9, 0));
        dto.setDureeMinutes(30);

        RendezVous existing = RendezVous.builder()
                .id(99L)
                .patient(patient)
                .medecin(medecin)
                .dateHeure(LocalDateTime.of(2026, 6, 1, 9, 15))
                .dureeMinutes(30)
                .statut(StatutRdv.PLANIFIE)
                .build();

        when(rendezVousRepository.findChevauchements(eq(1L), any(), any(), isNull()))
                .thenReturn(List.of(existing));

        assertThatThrownBy(() -> rendezVousService.save(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("superpose");

        verify(rendezVousRepository, never()).save(any());
    }

    @Test
    void annuler_shouldSetStatutAnnule() {
        RendezVous rdv = RendezVous.builder()
                .id(1L).patient(patient).medecin(medecin)
                .dateHeure(LocalDateTime.now()).dureeMinutes(30)
                .statut(StatutRdv.PLANIFIE).build();

        when(rendezVousRepository.findById(1L)).thenReturn(Optional.of(rdv));
        when(rendezVousRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        rendezVousService.annuler(1L);

        assertThat(rdv.getStatut()).isEqualTo(StatutRdv.ANNULE);
    }
}
