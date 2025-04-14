package com.example.Soutenances.Services;

import com.example.Soutenances.Entities.Duree_departement;
import com.example.Soutenances.Repositories.DureeDepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DureeDepartementService {
    @Autowired
    private DureeDepartementRepository repository;

    public Duree_departement saveDureeDepartement(Duree_departement duree) {
        return repository.save(duree);
    }


    public Duree_departement getDureeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donnée non trouvée pour l'ID : " + id));
    }

    public void deleteDuree(Long id) {
        repository.deleteById(id);
    }

    public Duree_departement updateDuree(Long id, Duree_departement newData) {
        Duree_departement existing = getDureeById(id);
        existing.setNomDept(newData.getNomDept());
        existing.setFiliere(newData.getFiliere());
        existing.setDate_debut(newData.getDate_debut());
        existing.setDate_fin(newData.getDate_fin());
        return repository.save(existing);
    }


}
