package com.example.Soutenances.Controllers;

import com.example.Soutenances.Services.SallesDisposServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/salles")
public class SallesDisposController {

    private static final Logger logger = Logger.getLogger(SoutenanceController.class.getName());

    @Autowired
    private SallesDisposServices sallesDisposService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<String> uploadSallesDisposFile(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: " + authentication.getName() + ", Roles: " + authentication.getAuthorities());

        try {
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body("Seuls les fichiers .xlsx sont acceptés !");
            }

            // Ensure the file is not empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Le fichier est vide !");
            }

            sallesDisposService.saveSallesDisposFile(file);
            return ResponseEntity.ok("Fichier de salles dispos importé avec succès !");
        } catch (Exception e) {
            logger.severe("Erreur lors de l'importation du fichier de salles dispos : " + e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de l'importation du fichier de salles dispos.");
        }
    }

}
