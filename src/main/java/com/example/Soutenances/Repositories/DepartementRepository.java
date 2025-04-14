package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.Departement;
import com.example.Soutenances.Entities.NomDepartement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    Optional<Departement> findByNomDept(NomDepartement nomDept);


}
