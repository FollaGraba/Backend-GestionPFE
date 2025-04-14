package com.example.Soutenances.Services;

import com.example.Soutenances.Entities.Departement;
import com.example.Soutenances.Entities.NomFiliere;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Repositories.DepartementRepository;
import com.example.Soutenances.Repositories.SoutenancesRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

@Service
public class UploadSoutenancesService {

    private static final Logger logger = Logger.getLogger(UploadSoutenancesService.class.getName());

    @Autowired
    private SoutenancesRepository soutenancesRepository;
    @Autowired
    private DepartementRepository departementRepository;

    public void saveSoutenancesFile(MultipartFile file, Long departementId, NomFiliere nomFiliere) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row if present
            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip the header
            }

            // Retrieve the department based on the provided ID
            Departement departement = departementRepository.findById(departementId)
                    .orElseThrow(() -> new RuntimeException("Département non trouvé"));

            List<Soutenances> soutenancesList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Soutenances soutenance = new Soutenances();

                // Populate the soutenance fields from the Excel row
                soutenance.setNomEtudiant(getStringCellValue(row.getCell(0)));
                soutenance.setEmail(getStringCellValue(row.getCell(1)));
                soutenance.setTitreSujet(getStringCellValue(row.getCell(2)));
                soutenance.setEncadrant(getStringCellValue(row.getCell(3)));
                soutenance.setPresident(getStringCellValue(row.getCell(4)));
                soutenance.setRapporteur(getStringCellValue(row.getCell(5)));
                soutenance.setDateSoutenance(getStringCellValue(row.getCell(6)));
                soutenance.setHeureSoutenance(getStringCellValue(row.getCell(7)));
                soutenance.setSalleSoutenance(getStringCellValue(row.getCell(8)));

                // Set the Departement object
                soutenance.setDepartement(departement);
//                soutenance.setNomDept(departement.getNomDept().name());
              //  soutenance.setFiliere(nomFiliere);
                soutenancesList.add(soutenance);
            }

            // Save in batch for better performance
            soutenancesRepository.saveAll(soutenancesList);
        } catch (IOException e) {
            logger.severe("Erreur lors de la lecture du fichier Excel : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'importation du fichier.", e);
        }
    }
    // Utility method to safely get string cell values
    private String getStringCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return null;
    }


//    public void deleteSoutenance(Long id) {
//       if (soutenancesRepository.existsById(id)) {
//        soutenancesRepository.deleteById(id);
//         logger.info("Soutenance supprimée avec succès : ID = " + id);
//        } else {
//          throw new IllegalArgumentException("Soutenance introuvable !");
//    }}
//
//    public Optional<Soutenances> updatePresident(Long id, String president) {
//     Optional<Soutenances> optionalSoutenance = soutenancesRepository.findById(id);
//     if (optionalSoutenance.isPresent()) {
//          Soutenances soutenance = optionalSoutenance.get();
//           soutenance.setPresident(president);
//        soutenancesRepository.save(soutenance);
//        } else {
//           throw new IllegalArgumentException("Soutenance introuvable !");
//      }
//     return optionalSoutenance;
//    }
//
// public Optional<Soutenances> updateRapporteur(Long id, String rapporteur) {
//  Optional<Soutenances> optionalSoutenance = soutenancesRepository.findById(id);
//        if (optionalSoutenance.isPresent()) {
//      Soutenances soutenance = optionalSoutenance.get();
//            soutenance.setRapporteur(rapporteur);
//           soutenancesRepository.save(soutenance);
//       } else {
//       throw new IllegalArgumentException("Soutenance introuvable !");
//       }
//     return optionalSoutenance;
//   }

    public List<Soutenances> getSoutenancesByDepartement(Departement departement) {
//        List<Soutenances> soutenances = soutenancesRepository.findByDepartement(departement);
//        if (soutenances.isEmpty()) {
//            throw new RuntimeException("Aucune soutenance trouvée pour le département : " + departement.getNomDept());
//        }
        return null;
    }

    public List<Soutenances> findSoutenancesByFiliere(NomFiliere filiere) {
//        List<Soutenances> soutenances = soutenancesRepository.findByFiliere(filiere);
//        if (soutenances.isEmpty()) {
//            throw new RuntimeException("Aucune soutenance trouvée pour la filière : " + filiere);
//        }
        return null;
    }



}
