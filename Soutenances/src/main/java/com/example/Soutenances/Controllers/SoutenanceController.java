package com.example.Soutenances.Controllers;

import com.example.Soutenances.DTO.SoutenanceDTO;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Services.SoutenanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soutenance")
public class SoutenanceController {

    private final SoutenanceService soutenanceService;

    public SoutenanceController(SoutenanceService soutenanceService) {
        this.soutenanceService = soutenanceService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Soutenances>> getAllSoutenances() {
        return ResponseEntity.ok(soutenanceService.getAllSoutenances());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSoutenance(@PathVariable Long id) { // Correction : Changement de int à Long
        try {
            soutenanceService.deleteSoutenance(id);
            return ResponseEntity.ok("Soutenance supprimée avec succès !");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Soutenances> updateSoutenance(@PathVariable Long id, @RequestBody SoutenanceDTO soutenanceDTO) { // Correction : Changement de int à Long
        try {
            Soutenances updatedSoutenance = soutenanceService.updateSoutenance(id, soutenanceDTO);
            return ResponseEntity.ok(updatedSoutenance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou une exception plus spécifique
        }
    }
}