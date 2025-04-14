package com.example.Soutenances.Entities;

import jakarta.persistence.*;

@Entity
@Table(name ="duree_departement_filiere")
public class Duree_departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom_departement", columnDefinition = "VARCHAR(255)" , nullable = false)
    private String nomDept;

    @Column(name = "filiere", columnDefinition = "VARCHAR(255)" , nullable = false)
    @Enumerated(EnumType.STRING)
    private NomFiliere filiere;

    private String date_debut;

    private String date_fin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDept() {
        return nomDept;
    }

    public void setNomDept(String nomDept) {
        this.nomDept = nomDept;
    }

    public NomFiliere getFiliere() {
        return filiere;
    }

    public void setFiliere(NomFiliere filiere) {
        this.filiere = filiere;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }
}
