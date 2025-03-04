package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisponibilteRepository extends JpaRepository<Disponibilite,Long> {
    Optional<Disponibilite> findByIdProfesseur(Long idProfesseur);
}
