package com.fst.cabinet.service;

import com.fst.cabinet.dto.RendezVousDto;
import com.fst.cabinet.entity.*;
import com.fst.cabinet.repository.RendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final PatientService patientService;
    private final MedecinService medecinService;

    @Transactional(readOnly = true)
    public List<RendezVous> findAll() {
        return rendezVousRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RendezVous findById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rendez-vous introuvable : " + id));
    }

    @Transactional(readOnly = true)
    public List<RendezVous> findByPatient(Long patientId) {
        return rendezVousRepository.findByPatientIdOrderByDateHeureDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<RendezVous> findByMedecin(Long medecinId) {
        return rendezVousRepository.findByMedecinIdOrderByDateHeureAsc(medecinId);
    }

    @Transactional(readOnly = true)
    public List<RendezVous> findDuJour(LocalDate date) {
        LocalDateTime debut = date.atStartOfDay();
        LocalDateTime fin = debut.plusDays(1);
        return rendezVousRepository.findByJour(debut, fin);
    }

    @Transactional(readOnly = true)
    public List<RendezVous> findDeLaSemaine(LocalDate lundi) {
        LocalDateTime debut = lundi.atStartOfDay();
        LocalDateTime fin = debut.plusDays(7);
        return rendezVousRepository.findBySemaine(debut, fin);
    }

    @Transactional(readOnly = true)
    public List<RendezVous> findEnAttente() {
        return rendezVousRepository.findByStatutOrderByDateHeureAsc(StatutRdv.PLANIFIE);
    }

    public RendezVous save(RendezVousDto dto) {
        verifierChevauchement(dto.getMedecinId(), dto.getDateHeure(), dto.getDureeMinutes(), null);
        RendezVous rdv = new RendezVous();
        fillFromDto(dto, rdv);
        return rendezVousRepository.save(rdv);
    }

    public RendezVous update(Long id, RendezVousDto dto) {
        RendezVous existing = findById(id);
        verifierChevauchement(dto.getMedecinId(), dto.getDateHeure(), dto.getDureeMinutes(), id);
        fillFromDto(dto, existing);
        return rendezVousRepository.save(existing);
    }

    public void annuler(Long id) {
        RendezVous rdv = findById(id);
        rdv.setStatut(StatutRdv.ANNULE);
        rendezVousRepository.save(rdv);
    }

    public void changerStatut(Long id, StatutRdv statut) {
        RendezVous rdv = findById(id);
        rdv.setStatut(statut);
        rendezVousRepository.save(rdv);
    }

    public void delete(Long id) {
        rendezVousRepository.delete(findById(id));
    }

    /** Validation métier : aucun chevauchement de RDV pour le même médecin */
    private void verifierChevauchement(Long medecinId, LocalDateTime debut, int duree, Long excludeId) {
        LocalDateTime fin = debut.plusMinutes(duree);
        List<RendezVous> chevauchements = rendezVousRepository.findChevauchements(medecinId, debut, fin, excludeId);
        if (!chevauchements.isEmpty()) {
            throw new IllegalStateException(
                "Ce médecin a déjà un rendez-vous qui se superpose à ce créneau (" +
                debut + " — durée " + duree + " min)."
            );
        }
    }

    public RendezVousDto toDto(RendezVous rdv) {
        return RendezVousDto.builder()
                .id(rdv.getId())
                .patientId(rdv.getPatient().getId())
                .medecinId(rdv.getMedecin().getId())
                .dateHeure(rdv.getDateHeure())
                .dureeMinutes(rdv.getDureeMinutes())
                .statut(rdv.getStatut())
                .motif(rdv.getMotif())
                .patientNomComplet(rdv.getPatient().getPrenom() + " " + rdv.getPatient().getNom())
                .medecinNomComplet("Dr. " + rdv.getMedecin().getPrenom() + " " + rdv.getMedecin().getNom())
                .build();
    }

    private void fillFromDto(RendezVousDto dto, RendezVous rdv) {
        rdv.setPatient(patientService.findById(dto.getPatientId()));
        rdv.setMedecin(medecinService.findById(dto.getMedecinId()));
        rdv.setDateHeure(dto.getDateHeure());
        rdv.setDureeMinutes(dto.getDureeMinutes());
        rdv.setMotif(dto.getMotif());
        if (dto.getStatut() != null) {
            rdv.setStatut(dto.getStatut());
        }
    }
}
