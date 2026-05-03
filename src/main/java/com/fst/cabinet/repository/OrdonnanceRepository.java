package com.fst.cabinet.repository;

import com.fst.cabinet.entity.Ordonnance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {

    Optional<Ordonnance> findByRendezVousId(Long rendezVousId);

    boolean existsByRendezVousId(Long rendezVousId);
}
