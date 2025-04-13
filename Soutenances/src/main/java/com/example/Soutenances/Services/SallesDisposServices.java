package com.example.Soutenances.Services;

import com.example.Soutenances.Entities.SallesDispos;
import com.example.Soutenances.Repositories.SallesDisposRepository;
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
import java.util.logging.Logger;

@Service
public class SallesDisposServices {

    private static final Logger logger = Logger.getLogger(SallesDisposServices.class.getName());

    @Autowired
    private SallesDisposRepository sallesDisposRepository;

    public void saveSallesDisposFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Vérification si le fichier Excel est vide
            if (sheet.getPhysicalNumberOfRows() <= 1) {
                throw new RuntimeException("Le fichier Excel est vide !");
            }

            saveSallesDispos(sheet);

        } catch (Exception e) {
            logger.severe("Erreur lors de l'importation des soutenances : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la lecture du fichier Excel", e);
        }
    }

    private void saveSallesDispos(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Ignorer l'en-tête
        List<SallesDispos> sallesList = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            SallesDispos salle = new SallesDispos();

            String dateValue = getCellValue(row.getCell(0)); // Colonne Date
            String salleValue = getCellValue(row.getCell(1)); // Colonne Salle

            // Vérification si les valeurs sont valides avant de les enregistrer
            if (dateValue != null && salleValue != null) {
                salle.setDate(dateValue.trim()); // Suppression des espaces
                salle.setSalle(salleValue.trim());
                sallesList.add(salle);
            } else {
                logger.warning("Ligne ignorée : données incomplètes (Date: " + dateValue + ", Salle: " + salleValue + ")");
            }
        }

        if (!sallesList.isEmpty()) {
            sallesDisposRepository.saveAll(sallesList);
            logger.info("Importation des salles disponibles terminée avec succès !");
        } else {
            logger.warning("Aucune salle valide à enregistrer.");
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null; // Éviter NullPointerException
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.format(cell.getDateCellValue());
                }
                return String.valueOf((int) cell.getNumericCellValue()); // Convertir en entier si nécessaire
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula(); // Gérer les formules si besoin
            default:
                return null; // Retourner null pour les types non pris en charge
        }
    }
}
