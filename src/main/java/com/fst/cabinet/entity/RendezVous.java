package com.fst.cabinet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "rendez_vous")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Min(5)
    @Column(nullable = false)
    private int dureeMinutes;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private StatutRdv statut = StatutRdv.PLANIFIE;

    private String motif;

    @OneToOne(mappedBy = "rendezVous", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ordonnance ordonnance;
}
