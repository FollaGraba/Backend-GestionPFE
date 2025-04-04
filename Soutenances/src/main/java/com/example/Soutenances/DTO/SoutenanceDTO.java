package com.example.Soutenances.DTO;

import lombok.Getter;
import lombok.Setter;


public class SoutenanceDTO {

    private int id; // Non utilisé dans le mapping, peut être supprimé si inutile
    private String nomEtudiant;
    private String emailEtudiant;
    private String titreSoutenance;
    private String idEncadrant;
    private String idPresident;
    private String idRapporteur;
    private String date; // Renommé pour correspondre à l'entité
    private String heure; // Renommé pour correspondre à l'entité
    private String salle; // Renommé pour correspondre à l'entité

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }
}