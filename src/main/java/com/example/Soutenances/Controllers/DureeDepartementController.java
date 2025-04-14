package com.example.Soutenances.Controllers;

import com.example.Soutenances.Entities.Duree_departement;
import com.example.Soutenances.Services.DureeDepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/duree")
public class DureeDepartementController {

    @Autowired
    private DureeDepartementService service;

    @PostMapping("/ajouter")
    public Duree_departement create(@RequestBody Duree_departement duree) {
        return service.saveDureeDepartement(duree);
    }


    @GetMapping("/{id}")
    public Duree_departement getById(@PathVariable Long id) {
        return service.getDureeById(id);
    }

    @PutMapping("/{id}")
    public Duree_departement update(@PathVariable Long id, @RequestBody Duree_departement duree) {
        return service.updateDuree(id, duree);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteDuree(id);
    }
}
