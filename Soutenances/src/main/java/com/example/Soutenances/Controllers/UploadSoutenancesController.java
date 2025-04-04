package com.example.Soutenances.Controllers;

import com.example.Soutenances.Entities.Departement;
import com.example.Soutenances.Entities.NomFiliere;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Repositories.DepartementRepository;
import com.example.Soutenances.Services.UploadSoutenancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/soutenances")
public class UploadSoutenancesController {

    private static final Logger logger = Logger.getLogger(UploadSoutenancesController.class.getName());

    @Autowired
    private UploadSoutenancesService soutenancesService;
    @Autowired
    private DepartementRepository departementRepository;

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



//     @DeleteMapping("/supprimer/{id}")
//    public ResponseEntity<?> deleteSoutenance(@PathVariable Long id) {
//        soutenancesService.deleteSoutenance(id);
//        return ResponseEntity.ok("Soutenance supprimée avec succès");
//    }
//

//    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
//    @PutMapping("/president/{id}")
//    public ResponseEntity<?> updatePresident(@PathVariable Long id, @RequestParam String president) {
//        Optional<Soutenances> updatedSoutenance = soutenancesService.updatePresident(id, president);
//        return updatedSoutenance.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
//    @PutMapping("/rapporteur/{id}")
//    public ResponseEntity<?> updateRapporteur(@PathVariable Long id, @RequestParam String rapporteur) {
//        Optional<Soutenances> updatedSoutenance = soutenancesService.updateRapporteur(id, rapporteur);
//        return updatedSoutenance.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//
//    }
    @GetMapping("/departement/{departementId}")

    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<List<Map<String, Object>>> getSoutenancesByDepartement(@PathVariable Long departementId) {
        // Récupérer le département par son ID
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));

        List<Soutenances> soutenances;
        try {
            soutenances = soutenancesService.getSoutenancesByDepartement(departement);

            List<Map<String, Object>> response = soutenances.stream().map(soutenancesItem -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", soutenancesItem.getId());
                map.put("nomEtudiant", soutenancesItem.getNomEtudiant());
                map.put("email", soutenancesItem.getEmail());
                map.put("titreSujet", soutenancesItem.getTitre());
                map.put("encadrant", soutenancesItem.getEncadrant());
                map.put("president", soutenancesItem.getPresident());
                map.put("rapporteur", soutenancesItem.getRapporteur());
                map.put("dateSoutenance", soutenancesItem.getDate_soutenance());
                map.put("heureSoutenance", soutenancesItem.getHeure());
                map.put("salle", soutenancesItem.getSalle());
                map.put("filiere", soutenancesItem.getFiliere().name()); // Obtention du nom de la filière

                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build();
        }
    }


    @GetMapping("filiere/{nomFiliere}")
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<List<Map<String, Object>>> getSoutenancesByFiliere(@PathVariable String nomFiliere) {
        List<Soutenances> soutenances;
        try {
            soutenances = soutenancesService.findSoutenancesByFiliere(NomFiliere.valueOf(nomFiliere));

            List<Map<String, Object>> response = soutenances.stream().map(soutenancesItem -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", soutenancesItem.getId());
                map.put("nomEtudiant", soutenancesItem.getNomEtudiant());
                map.put("email", soutenancesItem.getEmail());
                map.put("titreSujet", soutenancesItem.getTitre());
                map.put("encadrant", soutenancesItem.getEncadrant());
                map.put("president", soutenancesItem.getPresident());
                map.put("rapporteur", soutenancesItem.getRapporteur());
                map.put("dateSoutenance", soutenancesItem.getDate_soutenance());
                map.put("heureSoutenance", soutenancesItem.getHeure());
                map.put("salle", soutenancesItem.getSalle());
                map.put("nomDepartement", soutenancesItem.getNomDept());

                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build();
        }
    }




}
