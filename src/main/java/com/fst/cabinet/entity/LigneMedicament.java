package com.fst.cabinet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "lignes_medicament")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneMedicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordonnance_id", nullable = false)
    private Ordonnance ordonnance;

    @NotBlank
    @Column(nullable = false)
    private String nomMedicament;

    private String posologie;

    private String duree;
}
