package com.example.Soutenances.Services;

import com.example.Soutenances.DTO.SoutenanceDTO;
import com.example.Soutenances.Entities.Soutenances;
import com.example.Soutenances.Repositories.SoutenancesRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoutenanceService {
    @Autowired
    private SoutenancesRepository soutenancesRepository;

    // Mapper les données du front-end au modèle backend
    private Soutenances mapToSoutenancesEntity(SoutenanceDTO dto) {
        Soutenances soutenance = new Soutenances();
        soutenance.setNomEtudiant(dto.getNomEtudiant());
        soutenance.setEmail(dto.getEmailEtudiant());
        soutenance.setTitre(dto.getTitreSoutenance());
        soutenance.setEncadrant(dto.getIdEncadrant());
        soutenance.setPresident(dto.getIdPresident());
        soutenance.setRapporteur(dto.getIdRapporteur());
        soutenance.setDate(dto.getDateSoutenance());
        soutenance.setHeure(dto.getHeureSoutenance());
        soutenance.setSalle(dto.getIdSalle());
        return soutenance;
    }

    public List<Soutenances> saveAllSoutenances(List<SoutenanceDTO> dtoList) {
        List<Soutenances> soutenances = dtoList.stream()
                .map(this::mapToSoutenancesEntity)
                .collect(Collectors.toList());
        return soutenancesRepository.saveAll(soutenances);
    }
}
