package com.fst.cabinet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MedecinDto {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @NotBlank(message = "Le numéro d'ordre est obligatoire")
    private String numeroOrdre;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String telephone;

    @Email(message = "L'email n'est pas valide")
    private String email;

    private String horairesDisponibilite;

    private boolean actif = true;
}
