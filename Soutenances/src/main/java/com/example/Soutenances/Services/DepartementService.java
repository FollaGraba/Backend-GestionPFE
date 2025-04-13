package com.example.Soutenances.Services;

import com.example.Soutenances.Entities.Departement;
import com.example.Soutenances.Entities.NomDepartement;
import com.example.Soutenances.Entities.NomFiliere;
import com.example.Soutenances.Repositories.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    public Departement createDepartement(NomDepartement nomDept) {
        Departement departement = new Departement();
        departement.setNomDept(nomDept);

        // Chaque département doit avoir ces 3 filières
        List<NomFiliere> filieres = Arrays.asList(
                NomFiliere.LICENCE,
                NomFiliere.MASTER,
                NomFiliere.CYCLE_INGENIEUR
        );

        departement.setFilieres(filieres);
        return departementRepository.save(departement);
    }

    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }
}
