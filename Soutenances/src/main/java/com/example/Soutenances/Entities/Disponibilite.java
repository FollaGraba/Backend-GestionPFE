package com.example.Soutenances.Entities;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "disponibilites")
public class Disponibilite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idProfesseur;

    @ElementCollection
    private Set<LocalDateTime> datesDisponibles; // Liste des dates et heures disponibles

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProfesseur() {
        return idProfesseur;
    }

    public void setIdProfesseur(Long idProfesseur) {
        this.idProfesseur = idProfesseur;
    }

    public Set<LocalDateTime> getDatesDisponibles() {
        return datesDisponibles;
    }

    public void setDatesDisponibles(Set<LocalDateTime> datesDisponibles) {
        this.datesDisponibles = datesDisponibles;
    }
}

