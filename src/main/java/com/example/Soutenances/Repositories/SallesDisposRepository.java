package com.example.Soutenances.Repositories;


import com.example.Soutenances.Entities.Departement;
import com.example.Soutenances.Entities.NomDepartement;
import com.example.Soutenances.Entities.SallesDispos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SallesDisposRepository extends JpaRepository<SallesDispos, Long> {
    List<SallesDispos> findByDepartement(Departement departement);
    List<SallesDispos> findAll();
    List<SallesDispos> findByDate(String date);

}
