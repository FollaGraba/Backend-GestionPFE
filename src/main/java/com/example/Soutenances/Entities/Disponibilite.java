package com.example.Soutenances.Entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "disponibilites")
public class Disponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idProfesseur;

    @OneToMany(mappedBy = "disponibilite", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DisponibiliteDate> datesDisponibles;

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

    public Set<DisponibiliteDate> getDatesDisponibles() {
        return datesDisponibles;
    }

    public void setDatesDisponibles(Set<DisponibiliteDate> datesDisponibles) {
        this.datesDisponibles = datesDisponibles;
    }
}