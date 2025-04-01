package com.example.Soutenances.Services;

import com.example.Soutenances.Entities.SallesDispos;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Repositories.SallesDisposRepository;
import com.example.Soutenances.Repositories.SoutenancesRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ExcelService {

    private static final Logger logger = Logger.getLogger(ExcelService.class.getName());

    @Autowired
    private SallesDisposRepository sallesDisposRepository;

    @Autowired
    private SoutenancesRepository soutenancesRepository;

    public void saveExcelFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            String filename = file.getOriginalFilename();

            if ("salles_dispos.xlsx".equalsIgnoreCase(filename)) {
                saveSallesDispos(sheet);
            } else if ("soutenances.xlsx".equalsIgnoreCase(filename)) {
                saveSoutenances(sheet);
            } else {
                logger.warning("Fichier non reconnu : " + filename);
                throw new IllegalArgumentException("Fichier non reconnu !");
            }

        } catch (Exception e) {
            logger.severe("Erreur lors du traitement du fichier Excel : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la lecture du fichier Excel", e);
        }
    }

    private void saveSallesDispos(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Ignore header
        List<SallesDispos> sallesList = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            SallesDispos salle = new SallesDispos();

            String dateValue = getCellValue(row.getCell(0)); // Column Date
            String salleValue = getCellValue(row.getCell(1)); // Column Salle

            // Log the values being read
            logger.info("Row data - Date: " + dateValue + ", Salle: " + salleValue);

            salle.setDate(dateValue); // Set the date
            salle.setSalle(salleValue); // Set the salle

            sallesList.add(salle);
        }

        sallesDisposRepository.saveAll(sallesList);
        logger.info("Importation des salles disponibles terminée !");
    }


    private void saveSoutenances(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Ignorer l'en-tête
        List<Soutenances> soutenancesList = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Soutenances soutenance = new Soutenances();
            soutenance.setNomEtudiant(getCellValue(row.getCell(0)));
            soutenance.setEmail(getCellValue(row.getCell(1)));
            soutenance.setTitre(getCellValue(row.getCell(2)));
            soutenance.setEncadrant(getCellValue(row.getCell(3)));

            soutenance.setPresident(getCellValue(row.getCell(4)));
            soutenance.setRapporteur(getCellValue(row.getCell(5)));
            soutenance.setDate(getCellValue(row.getCell(6)));
            soutenance.setHeure(getCellValue(row.getCell(7)));
            soutenance.setSalle(getCellValue(row.getCell(8)));

            soutenancesList.add(soutenance);
        }

        soutenancesRepository.saveAll(soutenancesList);
        logger.info("Importation des soutenances terminée !");
    }
    private String getCellValue(Cell cell) {
        if (cell == null) return null; // Return null for empty cells
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.format(cell.getDateCellValue());
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula(); // Handle formulas if needed
            default:
                return null; // Return null for unsupported types
        }

    }












//    // Supprimer une soutenance par ID
//    public void deleteSoutenance(Long id) {
//        if (soutenancesRepository.existsById(id)) {
//            soutenancesRepository.deleteById(id);
//            logger.info("Soutenance supprimée avec succès : ID = " + id);
//        } else {
//            logger.warning("Suppression échouée, soutenance introuvable : ID = " + id);
//            throw new IllegalArgumentException("Soutenance introuvable !");
//        }
//    }
//    // Modifier le président du jury d'une soutenance
//    public Optional<Soutenances> updatePresident(Long id, String president) {
//        Optional<Soutenances> optionalSoutenance = soutenancesRepository.findById(id);
//        if (optionalSoutenance.isPresent()) {
//            Soutenances soutenance = optionalSoutenance.get();
//            soutenance.setPresident(president);
//            soutenancesRepository.save(soutenance);
//            logger.info("Président mis à jour avec succès : ID = " + id);
//        } else {
//            logger.warning("Modification échouée, soutenance introuvable : ID = " + id);
//            throw new IllegalArgumentException("Soutenance introuvable !");
//        }
//        return optionalSoutenance;
//    }
//
//    // Modifier le rapporteur d'une soutenance
//    public Optional<Soutenances> updateRapporteur(Long id, String rapporteur) {
//        Optional<Soutenances> optionalSoutenance = soutenancesRepository.findById(id);
//        if (optionalSoutenance.isPresent()) {
//            Soutenances soutenance = optionalSoutenance.get();
//            soutenance.setRapporteur(rapporteur);
//            soutenancesRepository.save(soutenance);
//            logger.info("Rapporteur mis à jour avec succès : ID = " + id);
//        } else {
//            logger.warning("Modification échouée, soutenance introuvable : ID = " + id);
//            throw new IllegalArgumentException("Soutenance introuvable !");
//        }
//        return optionalSoutenance;
//    }
//

}

