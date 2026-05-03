package com.fst.cabinet.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordonnances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rendez_vous_id", nullable = false, unique = true)
    private RendezVous rendezVous;

    @Column(nullable = false)
    private LocalDate dateEmission;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @OneToMany(mappedBy = "ordonnance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneMedicament> medicaments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.dateEmission == null) {
            this.dateEmission = LocalDate.now();
        }
    }
}
