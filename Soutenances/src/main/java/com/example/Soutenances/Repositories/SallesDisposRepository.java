package com.example.Soutenances.Repositories;


import com.example.Soutenances.Entities.SallesDispos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SallesDisposRepository extends JpaRepository<SallesDispos, Long> {
}
