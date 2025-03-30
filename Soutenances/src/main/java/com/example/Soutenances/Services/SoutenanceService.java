package com.example.Soutenances.Services;

import com.example.Soutenances.DTO.SoutenanceDTO;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Repositories.SoutenancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoutenanceService {
    @Autowired
    private SoutenancesRepository soutenancesRepository;

    // Mapper les données du front-end au modèle backend
    private LocalDateTime parseDate(String dateString) {
        try {
            return LocalDateTime.parse(dateString);
        } catch (DateTimeParseException e) {
            return null;  // ou une valeur par défaut
        }
    }

    private Soutenances mapToSoutenancesEntity(SoutenanceDTO dto) {
        Soutenances soutenance = new Soutenances();

        soutenance.setNomEtudiant(dto.getNomEtudiant());
        soutenance.setEmail(dto.getEmailEtudiant());
        soutenance.setTitre(dto.getTitreSoutenance());

        // Assurez-vous que les IDs sont convertis en Long si nécessaire
        soutenance.setEncadrant(dto.getIdEncadrant() != null ? dto.getIdEncadrant() : "");  // Défaut si null
        soutenance.setPresident(dto.getIdPresident() != null ? dto.getIdPresident() : "");  // Défaut si null
        soutenance.setRapporteur(dto.getIdRapporteur() != null ? dto.getIdRapporteur() : "");  // Défaut si null

        // Utilisation de LocalDateTime pour la date si nécessaire
        soutenance.setDate(dto.getDateSoutenance());
        soutenance.setHeure(dto.getHeureSoutenance());

        // Vérifiez également que l'ID de la salle est correctement affecté
        soutenance.setSalle(dto.getIdSalle() != null ? dto.getIdSalle() : "");  // Défaut si null
        System.out.println(soutenance);
        return soutenance;
    }


    public List<Soutenances> saveAllSoutenances(List<SoutenanceDTO> dtoList) {
        List<Soutenances> soutenances = dtoList.stream()
                .map(this::mapToSoutenancesEntity)
                .collect(Collectors.toList());
        return soutenancesRepository.saveAll(soutenances);
    }
    // Method to get all soutenances from the database
    public List<Soutenances> getAllSoutenances() {
        return soutenancesRepository.findAll();  // Uses the method from JpaRepository to fetch all data
    }
    public void deleteSoutenance(int id) {
        if (!soutenancesRepository.existsById(id)) {
            throw new RuntimeException("Soutenance avec ID " + id + " non trouvée");
        }
        soutenancesRepository.deleteById((long) id);
    }
    // Méthode d'update de soutenance
    public Soutenances updateSoutenance(int id, SoutenanceDTO dto) {
        // Vérification si la soutenance existe
        Soutenances existingSoutenance = soutenancesRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Soutenance avec ID " + id + " non trouvée"));

        // Mise à jour des champs de la soutenance existante avec les nouvelles valeurs
        existingSoutenance.setNomEtudiant(dto.getNomEtudiant());
        existingSoutenance.setEmail(dto.getEmailEtudiant());
        existingSoutenance.setTitre(dto.getTitreSoutenance());

        // Les champs encadrant, président, rapporteur peuvent être mis à jour selon les nouvelles valeurs
        if (dto.getIdEncadrant() != null) {
            existingSoutenance.setEncadrant(dto.getIdEncadrant());
        }
        if (dto.getIdPresident() != null) {
            existingSoutenance.setPresident(dto.getIdPresident());
        }
        if (dto.getIdRapporteur() != null) {
            existingSoutenance.setRapporteur(dto.getIdRapporteur());
        }

        // Mise à jour de la date et de l'heure si elles sont fournies
        if (dto.getDateSoutenance() != null) {
            existingSoutenance.setDate(dto.getDateSoutenance());
        }
        if (dto.getHeureSoutenance() != null) {
            existingSoutenance.setHeure(dto.getHeureSoutenance());
        }

        // Mise à jour de la salle si elle est fournie
        if (dto.getIdSalle() != null) {
            existingSoutenance.setSalle(dto.getIdSalle());
        }

        // Sauvegarde les changements
        return soutenancesRepository.save(existingSoutenance);
    }
}
