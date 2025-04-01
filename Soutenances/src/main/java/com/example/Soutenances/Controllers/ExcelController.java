package com.example.Soutenances.Controllers;

import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    private static final Logger logger = Logger.getLogger(ExcelController.class.getName());

    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: " + authentication.getName() + ", Roles: " + authentication.getAuthorities());

        try {
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body("Seuls les fichiers .xlsx sont acceptés !");
            }

            excelService.saveExcelFile(file);
            return ResponseEntity.ok("Fichier importé avec succès !");
        } catch (Exception e) {
            logger.severe("Erreur lors de l'importation du fichier : " + e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de l'importation du fichier.");
        }
    }

//    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
//    @DeleteMapping("/soutenances/supprimer/{id}")
//    public ResponseEntity<?> deleteSoutenance(@PathVariable Long id) {
//        excelService.deleteSoutenance(id);
//        return ResponseEntity.ok("Soutenance supprimée avec succès");
//    }


//    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
//    @PutMapping("/soutenances/president/{id}")
//    public ResponseEntity<?> updatePresident(@PathVariable Long id, @RequestParam String president) {
//        Optional<Soutenances> updatedSoutenance = excelService.updatePresident(id, president);
//        return updatedSoutenance.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
//    @PutMapping("/soutenances/rapporteur/{id}")
//    public ResponseEntity<?> updateRapporteur(@PathVariable Long id, @RequestParam String rapporteur) {
//        Optional<Soutenances> updatedSoutenance = excelService.updateRapporteur(id, rapporteur);
//        return updatedSoutenance.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }


}
