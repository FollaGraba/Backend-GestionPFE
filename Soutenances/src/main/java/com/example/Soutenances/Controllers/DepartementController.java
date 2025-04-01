package com.example.Soutenances.Controllers;

import com.example.Soutenances.Entities.Departement;
import com.example.Soutenances.Entities.NomDepartement;
import com.example.Soutenances.Services.DepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departement")
public class DepartementController {

    @Autowired
    private DepartementService departementService;

    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouterDepartement(@RequestParam NomDepartement nomDept) {
        Departement departement = departementService.createDepartement(nomDept);
        return ResponseEntity.ok(departement);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Departement>> getDepartements() {
        return ResponseEntity.ok(departementService.getAllDepartements());
    }
}
