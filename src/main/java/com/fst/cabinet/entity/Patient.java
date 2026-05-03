package com.fst.cabinet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 8)
    @NotBlank
    private String cin;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String prenom;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String telephone;

    @Email
    private String email;

    @Column(columnDefinition = "TEXT")
    private String antecedents;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RendezVous> rendezVous = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }
}
