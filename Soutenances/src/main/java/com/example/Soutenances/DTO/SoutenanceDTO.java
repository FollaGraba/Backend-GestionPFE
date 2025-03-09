package com.example.Soutenances.DTO;

import lombok.Getter;
import lombok.Setter;


public class SoutenanceDTO {
    private String idSoutenance;
    private String nomEtudiant;
    private String emailEtudiant;
    private String titreSoutenance;
    private String idEncadrant;
    private String idPresident;
    private String idRapporteur;
    private String dateSoutenance;
    private String heureSoutenance;
    private String idSalle;

    public String getIdSoutenance() {
        return idSoutenance;
    }

    public void setIdSoutenance(String idSoutenance) {
        this.idSoutenance = idSoutenance;
    }

    public String getNomEtudiant() {
        return nomEtudiant;
    }

    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }

    public String getEmailEtudiant() {
        return emailEtudiant;
    }

    public void setEmailEtudiant(String emailEtudiant) {
        this.emailEtudiant = emailEtudiant;
    }

    public String getTitreSoutenance() {
        return titreSoutenance;
    }

    public void setTitreSoutenance(String titreSoutenance) {
        this.titreSoutenance = titreSoutenance;
    }

    public String getIdEncadrant() {
        return idEncadrant;
    }

    public void setIdEncadrant(String idEncadrant) {
        this.idEncadrant = idEncadrant;
    }

    public String getIdPresident() {
        return idPresident;
    }

    public void setIdPresident(String idPresident) {
        this.idPresident = idPresident;
    }

    public String getIdRapporteur() {
        return idRapporteur;
    }

    public void setIdRapporteur(String idRapporteur) {
        this.idRapporteur = idRapporteur;
    }

    public String getDateSoutenance() {
        return dateSoutenance;
    }

    public void setDateSoutenance(String dateSoutenance) {
        this.dateSoutenance = dateSoutenance;
    }

    public String getHeureSoutenance() {
        return heureSoutenance;
    }

    public void setHeureSoutenance(String heureSoutenance) {
        this.heureSoutenance = heureSoutenance;
    }

    public String getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(String idSalle) {
        this.idSalle = idSalle;
    }
}
