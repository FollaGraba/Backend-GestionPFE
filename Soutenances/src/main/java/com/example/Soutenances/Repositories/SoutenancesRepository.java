package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.Soutenances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoutenancesRepository extends JpaRepository<Soutenances, Long> {
}
