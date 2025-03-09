package com.example.Soutenances.Controllers;

import com.example.Soutenances.DTO.SoutenanceDTO;
import com.example.Soutenances.Entities.Disponibilite;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Services.DisponibiliteService;
import com.example.Soutenances.Services.SoutenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@RestController
@RequestMapping("/api/soutenance")
public class SoutenanceController {

    private final SoutenanceService soutenancesService;

    public SoutenanceController(SoutenanceService soutenanceService) {
        this.soutenancesService = soutenanceService;
    }


    // Endpoint pour sauvegarder plusieurs soutenances avec mapping
    @PostMapping("/saveAll")
    public List<Soutenances> saveAllSoutenances(@RequestBody List<SoutenanceDTO> soutenancesDTO) {
        return soutenancesService.saveAllSoutenances(soutenancesDTO);
    }
}



