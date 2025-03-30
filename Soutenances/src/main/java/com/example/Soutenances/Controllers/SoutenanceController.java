package com.example.Soutenances.Controllers;

import com.example.Soutenances.DTO.SoutenanceDTO;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Services.SoutenanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soutenance")
public class SoutenanceController {

    private final SoutenanceService soutenancesService;

    public SoutenanceController(SoutenanceService soutenanceService) {
        this.soutenancesService = soutenanceService;
    }

    @PostMapping("/saveAll")
    public ResponseEntity<List<Soutenances>> saveAllSoutenances(@RequestBody List<SoutenanceDTO> soutenancesDTO) {
        List<Soutenances> savedSoutenances = soutenancesService.saveAllSoutenances(soutenancesDTO);
        return ResponseEntity.ok(savedSoutenances);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Soutenances>> getAllSoutenances() {
        return ResponseEntity.ok(soutenancesService.getAllSoutenances());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSoutenance(@PathVariable int id) {
        try {
            soutenancesService.deleteSoutenance(id);
            return ResponseEntity.ok("Soutenance supprimée avec succès !");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // End-point pour la mise à jour de la soutenance
    @PutMapping("/update/{id}")
    public ResponseEntity<Soutenances> updateSoutenance(@PathVariable int id, @RequestBody SoutenanceDTO soutenanceDTO) {
        try {
            Soutenances updatedSoutenance = soutenancesService.updateSoutenance(id, soutenanceDTO);
            return ResponseEntity.ok(updatedSoutenance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Or a more specific exception
        }
    }



}
