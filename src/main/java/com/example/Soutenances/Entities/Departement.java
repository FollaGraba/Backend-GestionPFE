package com.example.Soutenances.Entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NomDepartement nomDept;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<NomFiliere> filieres;


    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomDepartement getNomDept() {
        return nomDept;
    }

    public void setNomDept(NomDepartement nomDept) {
        this.nomDept = nomDept;
    }

    public List<NomFiliere> getFilieres() {
        return filieres;
    }

    public void setFilieres(List<NomFiliere> filieres) {
        this.filieres = filieres;
    }


}
