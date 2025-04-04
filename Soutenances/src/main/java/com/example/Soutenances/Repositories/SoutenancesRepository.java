package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.Departement;
import com.example.Soutenances.Entities.NomFiliere;
import com.example.Soutenances.Entities.Soutenances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoutenancesRepository extends JpaRepository<Soutenances, Long> {
    List<Soutenances> findByDepartement(Departement departement);
    List<Soutenances> findByFiliere(NomFiliere filiere);

    boolean existsById(int id);
    List<Soutenances> findByEncadrant(String encadrant);

}
