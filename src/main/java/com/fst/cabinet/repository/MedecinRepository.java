package com.fst.cabinet.repository;

import com.fst.cabinet.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {

    List<Medecin> findByActifTrue();

    Optional<Medecin> findByNumeroOrdre(String numeroOrdre);

    boolean existsByNumeroOrdre(String numeroOrdre);

    boolean existsByNumeroOrdreAndIdNot(String numeroOrdre, Long id);
}
