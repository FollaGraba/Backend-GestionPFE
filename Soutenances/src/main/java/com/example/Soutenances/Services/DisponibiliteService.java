package com.example.Soutenances.Services;

import com.example.Soutenances.Entities.Disponibilite;
import com.example.Soutenances.Repositories.DisponibilteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DisponibiliteService {
    private final DisponibilteRepository disponibiliteRepository;

    public DisponibiliteService(DisponibilteRepository disponibiliteRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
    }


    public Disponibilite ajouterDisponibilite(Long idProfesseur, Set<java.time.LocalDateTime> dates) {
        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setIdProfesseur(idProfesseur);
        disponibilite.setDatesDisponibles(dates);
        return disponibiliteRepository.save(disponibilite);
    }


    public Set<java.time.LocalDateTime> getDisponibilites(Long idProfesseur) {
        Optional<Disponibilite> disponibilite = disponibiliteRepository.findByIdProfesseur(idProfesseur);
        return disponibilite.map(Disponibilite::getDatesDisponibles).orElse(null);
    }
}
