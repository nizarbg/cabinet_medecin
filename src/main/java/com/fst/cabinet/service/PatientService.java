package com.fst.cabinet.service;

import com.fst.cabinet.dto.PatientDto;
import com.fst.cabinet.entity.Patient;
import com.fst.cabinet.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable : " + id));
    }

    @Transactional(readOnly = true)
    public List<Patient> rechercher(String query) {
        return patientRepository.rechercher(query);
    }

    public Patient save(PatientDto dto) {
        if (patientRepository.existsByCin(dto.getCin())) {
            throw new IllegalStateException("Un patient avec ce CIN existe déjà.");
        }
        Patient patient = toEntity(dto, new Patient());
        return patientRepository.save(patient);
    }

    public Patient update(Long id, PatientDto dto) {
        Patient existing = findById(id);
        if (patientRepository.existsByCinAndIdNot(dto.getCin(), id)) {
            throw new IllegalStateException("Un autre patient possède déjà ce CIN.");
        }
        toEntity(dto, existing);
        return patientRepository.save(existing);
    }

    public void delete(Long id) {
        Patient patient = findById(id);
        patientRepository.delete(patient);
    }

    public PatientDto toDto(Patient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .cin(patient.getCin())
                .nom(patient.getNom())
                .prenom(patient.getPrenom())
                .dateNaissance(patient.getDateNaissance())
                .telephone(patient.getTelephone())
                .email(patient.getEmail())
                .antecedents(patient.getAntecedents())
                .build();
    }

    private Patient toEntity(PatientDto dto, Patient patient) {
        patient.setCin(dto.getCin());
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setTelephone(dto.getTelephone());
        patient.setEmail(dto.getEmail());
        patient.setAntecedents(dto.getAntecedents());
        return patient;
    }
}
