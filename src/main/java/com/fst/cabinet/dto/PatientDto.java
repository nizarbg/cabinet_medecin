package com.fst.cabinet.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PatientDto {

    private Long id;

    @NotBlank(message = "Le CIN est obligatoire")
    @Size(min = 8, max = 8, message = "Le CIN doit contenir exactement 8 caractères")
    private String cin;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotNull(message = "La date de naissance est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String telephone;

    @Email(message = "L'email n'est pas valide")
    private String email;

    private String antecedents;
}
