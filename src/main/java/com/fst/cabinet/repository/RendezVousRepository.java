package com.fst.cabinet.repository;

import com.fst.cabinet.entity.RendezVous;
import com.fst.cabinet.entity.StatutRdv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    List<RendezVous> findByMedecinIdOrderByDateHeureAsc(Long medecinId);

    List<RendezVous> findByPatientIdOrderByDateHeureDesc(Long patientId);

    /** RDV d'un médecin pour une journée donnée */
    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId " +
           "AND r.dateHeure >= :debut AND r.dateHeure < :fin " +
           "AND r.statut <> 'ANNULE' ORDER BY r.dateHeure")
    List<RendezVous> findByMedecinAndJour(@Param("medecinId") Long medecinId,
                                          @Param("debut") LocalDateTime debut,
                                          @Param("fin") LocalDateTime fin);

    /** Tous les RDV d'une journée (dashboard) */
    @Query("SELECT r FROM RendezVous r WHERE r.dateHeure >= :debut AND r.dateHeure < :fin " +
           "AND r.statut <> 'ANNULE' ORDER BY r.dateHeure")
    List<RendezVous> findByJour(@Param("debut") LocalDateTime debut,
                                @Param("fin") LocalDateTime fin);

    /** RDV de la semaine (dashboard) */
    @Query("SELECT r FROM RendezVous r WHERE r.dateHeure >= :debut AND r.dateHeure < :fin " +
           "AND r.statut <> 'ANNULE' ORDER BY r.dateHeure")
    List<RendezVous> findBySemaine(@Param("debut") LocalDateTime debut,
                                   @Param("fin") LocalDateTime fin);

    /** Vérification de chevauchement : RDV actifs du médecin qui se superposent à [debut, fin[ */
    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId " +
           "AND r.statut NOT IN ('ANNULE', 'TERMINE') " +
           "AND r.dateHeure < :fin " +
           "AND FUNCTION('ADDTIME', r.dateHeure, FUNCTION('SEC_TO_TIME', r.dureeMinutes * 60)) > :debut " +
           "AND (:excludeId IS NULL OR r.id <> :excludeId)")
    List<RendezVous> findChevauchements(@Param("medecinId") Long medecinId,
                                        @Param("debut") LocalDateTime debut,
                                        @Param("fin") LocalDateTime fin,
                                        @Param("excludeId") Long excludeId);

    /** Patients en attente (statut PLANIFIE) */
    List<RendezVous> findByStatutOrderByDateHeureAsc(StatutRdv statut);

    /** RDV d'un médecin sur une semaine */
    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId " +
           "AND r.dateHeure >= :debut AND r.dateHeure < :fin " +
           "ORDER BY r.dateHeure")
    List<RendezVous> findByMedecinAndSemaine(@Param("medecinId") Long medecinId,
                                              @Param("debut") LocalDateTime debut,
                                              @Param("fin") LocalDateTime fin);
}
