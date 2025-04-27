package com.example.Soutenances.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "salles_dispos")
public class SallesDispos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSalle;

    private String date;
    private String salle;

    @ManyToOne // Définir la relation avec Departement
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    public Long getIdSalle() {
        return idSalle;
    }

    public Departement getDepartement() {
        return departement;
    }

    public String getDate() {
        return date;
    }
    public String getSalle() {
        return salle;
    }




    public void setDate(String cellValue) {
        this.date = cellValue; // Assurez-vous d'assigner la valeur
    }

    public void setSalle(String cellValue) {
        this.salle = cellValue; // Assurez-vous d'assigner la valeur
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "SallesDispos{" +
                "idSalle=" + idSalle +
                ", date='" + date + '\'' +
                ", salle='" + salle + '\'' +
                ", departement=" + departement +
                '}';
    }
}
