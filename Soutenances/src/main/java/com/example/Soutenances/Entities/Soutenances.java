package com.example.Soutenances.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "soutenances")
public class Soutenances {
       @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nom_etudiant", columnDefinition = "VARCHAR(255)")
        private String nomEtudiant;

        @Column(name = "email", columnDefinition = "VARCHAR(255)")
        private String email;

        @Column(name = "titre", columnDefinition = "VARCHAR(255)")
        private String titre;

        @Column(name = "encadrant", columnDefinition = "VARCHAR(255)")
        private String encadrant;

        @Column(name = "president", columnDefinition = "VARCHAR(255)")
        private String president;

        @Column(name = "rapporteur", columnDefinition = "VARCHAR(255)")
        private String rapporteur;

        @Column(name = "date_soutenance", columnDefinition = "VARCHAR(255)")
        private String date;

        @Column(name = "heure", columnDefinition = "VARCHAR(255)")
        private String heure;

        @Column(name = "salle", columnDefinition = "VARCHAR(255)")
        private String salle;

 public void setId(Long id) {
  this.id = id;
 }

 public void setNomEtudiant(String nomEtudiant) {
  this.nomEtudiant = nomEtudiant;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public void setTitre(String titre) {
  this.titre = titre;
 }

 public void setEncadrant(String encadrant) {
  this.encadrant = encadrant;
 }

 public void setPresident(String president) {
  this.president = president;
 }

 public void setRapporteur(String rapporteur) {
  this.rapporteur = rapporteur;
 }

 public void setDate(String date) {
  this.date = date;
 }

 public void setHeure(String heure) {
  this.heure = heure;
 }

 public void setSalle(String salle) {
  this.salle = salle;
 }
}
