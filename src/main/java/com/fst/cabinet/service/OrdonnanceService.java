package com.fst.cabinet.service;

import com.fst.cabinet.dto.LigneMedicamentDto;
import com.fst.cabinet.dto.OrdonnanceDto;
import com.fst.cabinet.entity.LigneMedicament;
import com.fst.cabinet.entity.Ordonnance;
import com.fst.cabinet.entity.RendezVous;
import com.fst.cabinet.entity.StatutRdv;
import com.fst.cabinet.repository.OrdonnanceRepository;
import com.fst.cabinet.repository.RendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdonnanceService {

    private final OrdonnanceRepository ordonnanceRepository;
    private final RendezVousRepository rendezVousRepository;

    @Transactional(readOnly = true)
    public Ordonnance findById(Long id) {
        return ordonnanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordonnance introuvable : " + id));
    }

    @Transactional(readOnly = true)
    public Ordonnance findByRendezVousId(Long rdvId) {
        return ordonnanceRepository.findByRendezVousId(rdvId).orElse(null);
    }

    public Ordonnance save(OrdonnanceDto dto) {
        if (ordonnanceRepository.existsByRendezVousId(dto.getRendezVousId())) {
            throw new IllegalStateException("Une ordonnance existe déjà pour ce rendez-vous.");
        }
        RendezVous rdv = rendezVousRepository.findById(dto.getRendezVousId())
                .orElseThrow(() -> new IllegalArgumentException("Rendez-vous introuvable."));

        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setRendezVous(rdv);
        ordonnance.setDateEmission(LocalDate.now());
        ordonnance.setObservations(dto.getObservations());
        ordonnance.setMedicaments(buildLignes(dto.getMedicaments(), ordonnance));

        // Marquer le RDV comme terminé
        rdv.setStatut(StatutRdv.TERMINE);

        return ordonnanceRepository.save(ordonnance);
    }

    public Ordonnance update(Long id, OrdonnanceDto dto) {
        Ordonnance existing = findById(id);
        existing.setObservations(dto.getObservations());
        existing.getMedicaments().clear();
        existing.getMedicaments().addAll(buildLignes(dto.getMedicaments(), existing));
        return ordonnanceRepository.save(existing);
    }

    public void delete(Long id) {
        ordonnanceRepository.delete(findById(id));
    }

    public OrdonnanceDto toDto(Ordonnance o) {
        List<LigneMedicamentDto> lignes = o.getMedicaments().stream().map(l ->
                LigneMedicamentDto.builder()
                        .id(l.getId())
                        .nomMedicament(l.getNomMedicament())
                        .posologie(l.getPosologie())
                        .duree(l.getDuree())
                        .build()
        ).toList();
        return OrdonnanceDto.builder()
                .id(o.getId())
                .rendezVousId(o.getRendezVous().getId())
                .dateEmission(o.getDateEmission())
                .observations(o.getObservations())
                .medicaments(lignes)
                .build();
    }

    private List<LigneMedicament> buildLignes(List<LigneMedicamentDto> dtos, Ordonnance ordonnance) {
        List<LigneMedicament> lignes = new ArrayList<>();
        if (dtos == null) return lignes;
        for (LigneMedicamentDto d : dtos) {
            LigneMedicament l = new LigneMedicament();
            l.setOrdonnance(ordonnance);
            l.setNomMedicament(d.getNomMedicament());
            l.setPosologie(d.getPosologie());
            l.setDuree(d.getDuree());
            lignes.add(l);
        }
        return lignes;
    }
}
