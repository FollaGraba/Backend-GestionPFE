package com.example.Soutenances.Controllers;

import com.example.Soutenances.Entities.NomFiliere;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Services.UploadSoutenancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/soutenances")
public class UploadSoutenancesController {

    private static final Logger logger = Logger.getLogger(UploadSoutenancesController.class.getName());

    @Autowired
    private UploadSoutenancesService soutenancesService;

    @PostMapping("/upload-soutenances/{departementId}/{nomFiliere}")
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<String> uploadSoutenancesFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long departementId,
            @PathVariable NomFiliere nomFiliere) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: " + authentication.getName() + ", Roles: " + authentication.getAuthorities());

        try {
            // Check if the file is a valid Excel file
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body("Seuls les fichiers .xlsx sont acceptés !");
            }

            // Check if the file is empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Le fichier est vide !");
            }

            // Call the service to process the file
            soutenancesService.saveSoutenancesFile(file, departementId, nomFiliere);
            return ResponseEntity.ok("Fichier importé avec succès !");
        } catch (Exception e) {
            logger.severe("Erreur lors de l'importation du fichier de soutenance : " + e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de l'importation du fichier de soutenance.");
        }
    }


     @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> deleteSoutenance(@PathVariable Long id) {
        soutenancesService.deleteSoutenance(id);
        return ResponseEntity.ok("Soutenance supprimée avec succès");
    }


    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    @PutMapping("/president/{id}")
    public ResponseEntity<?> updatePresident(@PathVariable Long id, @RequestParam String president) {
        Optional<Soutenances> updatedSoutenance = soutenancesService.updatePresident(id, president);
        return updatedSoutenance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    @PutMapping("/rapporteur/{id}")
    public ResponseEntity<?> updateRapporteur(@PathVariable Long id, @RequestParam String rapporteur) {
        Optional<Soutenances> updatedSoutenance = soutenancesService.updateRapporteur(id, rapporteur);
        return updatedSoutenance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
    @GetMapping("/departement/{departementId}")
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<List<Soutenances>> getSoutenancesByDepartement(@PathVariable Long departementId) {
        List<Soutenances> soutenances = soutenancesService.getSoutenancesByDepartement(departementId);

        if (soutenances.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(soutenances);
    }


}
