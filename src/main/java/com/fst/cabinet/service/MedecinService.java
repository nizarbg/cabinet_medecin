package com.fst.cabinet.service;

import com.fst.cabinet.dto.MedecinDto;
import com.fst.cabinet.entity.Medecin;
import com.fst.cabinet.repository.MedecinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedecinService {

    private final MedecinRepository medecinRepository;

    @Transactional(readOnly = true)
    public List<Medecin> findAll() {
        return medecinRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Medecin> findActifs() {
        return medecinRepository.findByActifTrue();
    }

    @Transactional(readOnly = true)
    public Medecin findById(Long id) {
        return medecinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médecin introuvable : " + id));
    }

    public Medecin save(MedecinDto dto) {
        if (medecinRepository.existsByNumeroOrdre(dto.getNumeroOrdre())) {
            throw new IllegalStateException("Un médecin avec ce numéro d'ordre existe déjà.");
        }
        return medecinRepository.save(toEntity(dto, new Medecin()));
    }

    public Medecin update(Long id, MedecinDto dto) {
        Medecin existing = findById(id);
        if (medecinRepository.existsByNumeroOrdreAndIdNot(dto.getNumeroOrdre(), id)) {
            throw new IllegalStateException("Un autre médecin possède déjà ce numéro d'ordre.");
        }
        toEntity(dto, existing);
        return medecinRepository.save(existing);
    }

    public void delete(Long id) {
        medecinRepository.delete(findById(id));
    }

    public MedecinDto toDto(Medecin medecin) {
        return MedecinDto.builder()
                .id(medecin.getId())
                .nom(medecin.getNom())
                .prenom(medecin.getPrenom())
                .specialite(medecin.getSpecialite())
                .numeroOrdre(medecin.getNumeroOrdre())
                .telephone(medecin.getTelephone())
                .email(medecin.getEmail())
                .horairesDisponibilite(medecin.getHorairesDisponibilite())
                .actif(medecin.isActif())
                .build();
    }

    private Medecin toEntity(MedecinDto dto, Medecin medecin) {
        medecin.setNom(dto.getNom());
        medecin.setPrenom(dto.getPrenom());
        medecin.setSpecialite(dto.getSpecialite());
        medecin.setNumeroOrdre(dto.getNumeroOrdre());
        medecin.setTelephone(dto.getTelephone());
        medecin.setEmail(dto.getEmail());
        medecin.setHorairesDisponibilite(dto.getHorairesDisponibilite());
        medecin.setActif(dto.isActif());
        return medecin;
    }
}
