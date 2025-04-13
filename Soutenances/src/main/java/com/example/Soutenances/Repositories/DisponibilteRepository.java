package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisponibilteRepository extends JpaRepository<Disponibilite, Long> {
    Optional<Disponibilite> findByIdProfesseur(Long idProfesseur);

    @Modifying
    @Query("DELETE FROM Disponibilite d WHERE d.idProfesseur = :idProfesseur")
    void deleteAllByIdProfesseur(@Param("idProfesseur") Long idProfesseur);
}