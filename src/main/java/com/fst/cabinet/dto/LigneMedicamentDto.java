package com.fst.cabinet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneMedicamentDto {

    private Long id;

    @NotBlank(message = "Le nom du médicament est obligatoire")
    private String nomMedicament;

    private String posologie;

    private String duree;
}
