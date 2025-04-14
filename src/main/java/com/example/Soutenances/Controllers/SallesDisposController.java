package com.example.Soutenances.Controllers;

import com.example.Soutenances.Entities.SallesDispos;
import com.example.Soutenances.Services.SallesDisposServices;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/salles")
public class SallesDisposController {

    private static final Logger logger = Logger.getLogger(SoutenanceController.class.getName());



    @Autowired
    private SallesDisposServices sallesDisposService;

    @PostMapping("/upload/{nomDepartement}") // Inclure le nom du département dans l'URL
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<String> uploadSallesDisposFile(@PathVariable String nomDepartement, @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: " + authentication.getName() + ", Roles: " + authentication.getAuthorities());

        try {
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body("Seuls les fichiers .xlsx sont acceptés !");
            }

            // Vérifier si le fichier est vide
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Le fichier est vide !");
            }

            // Importer les données et récupérer le nom du département
            String nomSauvegarde = sallesDisposService.saveSallesDisposFile(file, nomDepartement);
            return ResponseEntity.ok("Fichier de salles dispos importé avec succès ! Département : " + nomSauvegarde);
        } catch (Exception e) {
            logger.severe("Erreur lors de l'importation du fichier de salles dispos : " + e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de l'importation du fichier de salles dispos.");
        }
    }
    // Endpoint to get all SallesDispos
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllSallesDispos() {
        List<SallesDispos> salles = sallesDisposService.getAllSallesDispos();

        List<Map<String, Object>> response = salles.stream().map(salle -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idSalle", salle.getIdSalle());
            map.put("date", salle.getDate());
            map.put("salle", salle.getSalle());
            map.put("nomDepartement", salle.getDepartement().getNomDept().name()); // juste le nom du département
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Endpoint to get SallesDispos by Departement
// Endpoint to get SallesDispos by Departement
    @GetMapping("/{nomDepartement}")
    public ResponseEntity<List<Map<String, Object>>> getSallesByDepartement(@PathVariable String nomDepartement) {
        List<SallesDispos> salles = sallesDisposService.getSallesByDepartement(nomDepartement);

        List<Map<String, Object>> response = salles.stream().map(salle -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idSalle", salle.getIdSalle());
            map.put("date", salle.getDate());
            map.put("salle", salle.getSalle());

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


}
