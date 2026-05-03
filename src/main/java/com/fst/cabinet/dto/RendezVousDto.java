package com.fst.cabinet.dto;

import com.fst.cabinet.entity.StatutRdv;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RendezVousDto {

    private Long id;

    @NotNull(message = "Le patient est obligatoire")
    private Long patientId;

    @NotNull(message = "Le médecin est obligatoire")
    private Long medecinId;

    @NotNull(message = "La date et heure sont obligatoires")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateHeure;

    @Min(value = 5, message = "La durée minimum est de 5 minutes")
    private int dureeMinutes = 30;

    private StatutRdv statut = StatutRdv.PLANIFIE;

    private String motif;

    // Affichage seulement
    private String patientNomComplet;
    private String medecinNomComplet;
}
