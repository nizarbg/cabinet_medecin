package com.fst.cabinet.repository;

import com.fst.cabinet.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByCin(String cin);

    boolean existsByCin(String cin);

    boolean existsByCinAndIdNot(String cin, Long id);

    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "p.cin LIKE CONCAT('%', :q, '%') OR " +
           "p.telephone LIKE CONCAT('%', :q, '%')")
    List<Patient> rechercher(@Param("q") String query);
}
