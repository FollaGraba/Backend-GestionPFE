package com.example.Soutenances.Controllers;

import com.example.Soutenances.DTO.DisponibiliteRequest;
import com.example.Soutenances.Services.DisponibiliteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilites")
@CrossOrigin(origins = "*")
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    public DisponibiliteController(DisponibiliteService disponibiliteService) {
        this.disponibiliteService = disponibiliteService;
    }

    /**
     * Endpoint pour ajouter des disponibilités pour un professeur.
     *
     * @param request La requête contenant les disponibilités.
     * @return Réponse indiquant le succès ou l'échec.
     */
    @PostMapping("/ajouter")
    public ResponseEntity<Void> ajouterDisponibilite(@RequestBody DisponibiliteRequest request) {
        disponibiliteService.ajouterDisponibilite(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint pour récupérer les disponibilités d'un professeur.
     *
     * @param idProfesseur L'ID du professeur.
     * @return Les disponibilités ou une réponse vide si aucune n'existe.
     */
    @GetMapping("/getbyidprof/{idProfesseur}")
    public ResponseEntity<List<?>> getDisponibilites(@PathVariable Long idProfesseur) {
        List<?> disponibilites = disponibiliteService.getDisponibilites(idProfesseur);
        if (disponibilites == null || disponibilites.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(disponibilites);
    }
}