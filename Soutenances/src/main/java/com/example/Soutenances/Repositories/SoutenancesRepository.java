package com.example.Soutenances.Repositories;

import com.example.Soutenances.Entities.Soutenances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoutenancesRepository extends JpaRepository<Soutenances, Long> {
    List<Soutenances> findByDepartementId(Long departementId);
    boolean existsById(int id);

}
