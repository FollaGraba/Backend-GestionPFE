package com.example.Soutenances.Services;

import com.example.Soutenances.DTO.SoutenanceDTO;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Repositories.SoutenancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoutenanceService {

    @Autowired
    private SoutenancesRepository soutenancesRepository;

    private Soutenances mapToSoutenancesEntity(SoutenanceDTO dto) {
        Soutenances soutenance = new Soutenances();
        soutenance.setNomEtudiant(dto.getNomEtudiant());
        soutenance.setEmail(dto.getEmailEtudiant());
        soutenance.setTitreSujet(dto.getTitreSoutenance()); // Correction : Mise à jour du nom du champ
        soutenance.setEncadrant(dto.getIdEncadrant() != null ? dto.getIdEncadrant() : "");
        soutenance.setPresident(dto.getIdPresident() != null ? dto.getIdPresident() : "");
        soutenance.setRapporteur(dto.getIdRapporteur() != null ? dto.getIdRapporteur() : "");
        soutenance.setDateSoutenance(dto.getDate()); // Correction : Mise à jour du nom du champ
        soutenance.setHeureSoutenance(dto.getHeure()); // Correction : Mise à jour du nom du champ
        soutenance.setSalleSoutenance(dto.getSalle()); // Correction : Mise à jour du nom du champ
        return soutenance;
    }

    public List<Soutenances> getAllSoutenances() {
        return soutenancesRepository.findAll();
    }

    public void deleteSoutenance(Long id) { // Correction : Changement de int à Long
        if (!soutenancesRepository.existsById(id)) {
            throw new RuntimeException("Soutenance avec ID " + id + " non trouvée");
        }
        soutenancesRepository.deleteById(id);
    }

    public Soutenances updateSoutenance(Long id, SoutenanceDTO dto) { // Correction : Changement de int à Long
        Soutenances existingSoutenance = soutenancesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soutenance avec ID " + id + " non trouvée"));
        existingSoutenance.setNomEtudiant(dto.getNomEtudiant());
        existingSoutenance.setEmail(dto.getEmailEtudiant());
        existingSoutenance.setTitreSujet(dto.getTitreSoutenance()); // Correction : Mise à jour du nom du champ
        if (dto.getIdEncadrant() != null) {
            existingSoutenance.setEncadrant(dto.getIdEncadrant());
        }
        if (dto.getIdPresident() != null) {
            existingSoutenance.setPresident(dto.getIdPresident());
        }
        if (dto.getIdRapporteur() != null) {
            existingSoutenance.setRapporteur(dto.getIdRapporteur());
        }
        if (dto.getDate() != null) {
            existingSoutenance.setDateSoutenance(dto.getDate()); // Correction : Mise à jour du nom du champ
        }
        if (dto.getHeure() != null) {
            existingSoutenance.setHeureSoutenance(dto.getHeure()); // Correction : Mise à jour du nom du champ
        }
        if (dto.getSalle() != null) {
            existingSoutenance.setSalleSoutenance(dto.getSalle()); // Correction : Mise à jour du nom du champ
        }
        return soutenancesRepository.save(existingSoutenance);
    }

    public List<Soutenances> getSoutenancesByEncadrant(String encadrant) {
        return soutenancesRepository.findByEncadrant(encadrant);
    }

    public List<Soutenances> getSoutenancesByUsername(String username) {
        return soutenancesRepository.findByEncadrantOrRapporteurOrPresident(username, username, username);
    }

}