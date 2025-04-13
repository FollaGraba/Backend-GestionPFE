package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Assurez-vous que cette annotation est bien présente
public interface DepartementRepository extends JpaRepository<Departement, Long> {
}
