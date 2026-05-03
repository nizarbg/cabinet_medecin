package com.fst.cabinet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrdonnanceDto {

    private Long id;

    @NotNull(message = "Le rendez-vous est obligatoire")
    private Long rendezVousId;

    private LocalDate dateEmission;

    private String observations;

    @Valid
    private List<LigneMedicamentDto> medicaments = new ArrayList<>();
}
