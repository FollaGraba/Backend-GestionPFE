package com.example.Soutenances.Controllers;

import com.example.Soutenances.Entities.Disponibilite;
import com.example.Soutenances.Services.DisponibiliteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/disponibilites")
public class DisponibiliteController {
    private final DisponibiliteService disponibiliteService;

    public DisponibiliteController(DisponibiliteService disponibiliteService) {
        this.disponibiliteService = disponibiliteService;
    }


    @PostMapping("/ajouter")
    public ResponseEntity<Disponibilite> ajouterDisponibilite(@RequestBody Disponibilite request) {
        Disponibilite disponibilite = disponibiliteService.ajouterDisponibilite(request.getIdProfesseur(), request.getDatesDisponibles());
        return ResponseEntity.ok(disponibilite);
    }


    @GetMapping("getbyidprof/{idProfesseur}")
    public ResponseEntity<Set<LocalDateTime>> getDisponibilites(@PathVariable Long idProfesseur) {
        return ResponseEntity.ok(disponibiliteService.getDisponibilites(idProfesseur));
    }
}
