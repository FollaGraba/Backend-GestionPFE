package com.example.Soutenances.Services;

import com.example.Soutenances.DTO.DisponibiliteRequest;
import com.example.Soutenances.Entities.Disponibilite;
import com.example.Soutenances.Entities.DisponibiliteDate;
import com.example.Soutenances.Repositories.DisponibiliteDateRepository;
import com.example.Soutenances.Repositories.DisponibilteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisponibiliteService {

    @Autowired
    private DisponibilteRepository disponibilteRepository;

    /**
     * Ajoute ou met à jour les disponibilités d'un professeur.
     *
     * @param request La requête contenant les disponibilités.
     */
    @Autowired
    private DisponibiliteDateRepository disponibiliteDateRepository;

    @Transactional
    public void ajouterDisponibilite(DisponibiliteRequest request) {
        Long idProfesseur = request.getIdProfesseur();

        // Récupérer l'ancienne disponibilité et la supprimer
        disponibilteRepository.findByIdProfesseur(idProfesseur).ifPresent(disponibilite -> {
            disponibiliteDateRepository.deleteByDisponibiliteId(disponibilite.getId());
            disponibilteRepository.delete(disponibilite);
        });

        // Ajouter la nouvelle disponibilité
        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setIdProfesseur(idProfesseur);

        Set<DisponibiliteDate> datesDisponibles = request.getDisponibilites().stream()
                .map(dto -> {
                    DisponibiliteDate dateDisponible = new DisponibiliteDate();
                    dateDisponible.setJour(dto.getJour());
                    dateDisponible.setSession(dto.getSession());
                    dateDisponible.setDisponibilite(disponibilite);
                    return dateDisponible;
                })
                .collect(Collectors.toSet());

        disponibilite.setDatesDisponibles(datesDisponibles);
        disponibilteRepository.save(disponibilite);
    }

    /**
     * Récupère les disponibilités d'un professeur.
     *
     * @param idProfesseur L'ID du professeur.
     * @return Liste des disponibilités.
     */
    public List<DisponibiliteDate> getDisponibilites(Long idProfesseur) {
        return (List<DisponibiliteDate>) disponibilteRepository.findByIdProfesseur(idProfesseur)
                .map(Disponibilite::getDatesDisponibles)
                .orElse(null);
    }
}