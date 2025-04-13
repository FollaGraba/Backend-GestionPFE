package com.example.Soutenances.Entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "disponibilite_dates")
public class DisponibiliteDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jour; // Date de disponibilité

    private String session; // Session au format "HH:mm - HH:mm"

    @ManyToOne
    @JoinColumn(name = "disponibilite_id", nullable = false, foreignKey = @ForeignKey(name = "fk_disponibilite", value = ConstraintMode.CONSTRAINT))
    private Disponibilite disponibilite;

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Disponibilite getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Disponibilite disponibilite) {
        this.disponibilite = disponibilite;
    }
}