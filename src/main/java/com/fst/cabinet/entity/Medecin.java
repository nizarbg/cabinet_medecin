package com.fst.cabinet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medecins")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String prenom;

    @NotBlank
    @Column(nullable = false)
    private String specialite;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String numeroOrdre;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String telephone;

    @Email
    private String email;

    /** Horaires de disponibilité (ex: "Lun-Ven 08:00-17:00") */
    private String horairesDisponibilite;

    @Column(nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RendezVous> rendezVous = new ArrayList<>();
}
