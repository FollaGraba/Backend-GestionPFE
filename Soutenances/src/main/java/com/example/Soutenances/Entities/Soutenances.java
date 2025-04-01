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
        private String nom_etudiant;

        @Column(name = "email", columnDefinition = "VARCHAR(255)")
        private String email;

        @Column(name = "titre_sujet ", columnDefinition = "VARCHAR(255)")
        private String titre_sujet;

        @Column(name = "encadrant", columnDefinition = "VARCHAR(255)")
        private String encadrant;

        @Column(name = "president", columnDefinition = "VARCHAR(255)")
        private String president;

        @Column(name = "rapporteur", columnDefinition = "VARCHAR(255)")
        private String rapporteur;

        @Column(name = "date_soutenance", columnDefinition = "VARCHAR(255)")
        private String date_soutenance;

        @Column(name = "heure_soutenance", columnDefinition = "VARCHAR(255)")
        private String heure_soutenance;

        @Column(name = "salle", columnDefinition = "VARCHAR(255)")
        private String salle_soutenance;
 @ManyToOne
 @JoinColumn(name = "departement_id", nullable = false) // Use a foreign key for the relationship
 private Departement departement;


 public void setId(Long id) {
  this.id = id;
 }

 public void setNomEtudiant(String nomEtudiant) {
  this.nom_etudiant = nomEtudiant;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public void setTitre(String titre) {
  this.titre_sujet = titre;
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
  this.date_soutenance = date;
 }

 public void setHeure(String heure) {
  this.heure_soutenance = heure;
 }

 public void setSalle(String salle) {
  this.salle_soutenance = salle;
 }


 public void setDepartement(Departement departement) {
  this.departement = departement;
 }
}
