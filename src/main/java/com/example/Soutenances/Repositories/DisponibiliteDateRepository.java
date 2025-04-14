package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.DisponibiliteDate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibiliteDateRepository extends JpaRepository<DisponibiliteDate, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM DisponibiliteDate d WHERE d.disponibilite.id = :disponibiliteId")
    void deleteByDisponibiliteId(@Param("disponibiliteId") Long disponibiliteId);

    List<DisponibiliteDate> findByJourAndSession(String jour, String creneau);
}

