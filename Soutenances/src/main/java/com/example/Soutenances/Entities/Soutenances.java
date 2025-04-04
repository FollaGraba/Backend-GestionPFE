package com.example.Soutenances.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "soutenances")
public class Soutenances {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

        @Column(name = "nom_etudiant", columnDefinition = "VARCHAR(255)")
        private String nom_etudiant;


 @Column(name = "email", nullable = false, length = 255)
 private String email;

        @Column(name = "titre_sujet ", columnDefinition = "VARCHAR(255)")
        private String titre_sujet;


 @Column(name = "encadrant", length = 255)
 private String encadrant;

 @Column(name = "president", length = 255)
 private String president;

 @Column(name = "rapporteur", length = 255)
 private String rapporteur;

 public String getDate_soutenance() {
  return date_soutenance;
 }

 @Column(name = "date_soutenance", columnDefinition = "VARCHAR(255)")
        private String date_soutenance;

        @Column(name = "heure_soutenance", columnDefinition = "VARCHAR(255)")
        private String heure_soutenance;

        @Column(name = "salle", columnDefinition = "VARCHAR(255)")
        private String salle_soutenance;

 public String getNomDept() {
  return nomDept;
 }

 public void setNomDept(String nomDept) {
  this.nomDept = nomDept;
 }

 public void setDepartement(Departement departement) {
  this.departement = departement;
 }

 @ManyToOne
 @JoinColumn(name = "departement_id", nullable = false)
 private Departement departement;

 @Column(name = "nom_departement", nullable = false)
 private String nomDept;
 @Column(name = "filiere", nullable = false)
 @Enumerated(EnumType.STRING)
  private NomFiliere filiere;

 // Getters et Setters
 public NomFiliere getFiliere() {
  return filiere;
 }

 public void setFiliere(NomFiliere filiere) {
  this.filiere = filiere;
 }


 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }

 public String getNomEtudiant() {
  return nom_etudiant;
 }

 public void setNomEtudiant(String nomEtudiant) {
  this.nom_etudiant = nomEtudiant;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public String getTitre() {
  return titre_sujet;
 }

 public void setTitre(String titre) {
  this.titre_sujet = titre;
 }

 public String getEncadrant() {
  return encadrant;
 }

 public void setEncadrant(String encadrant) {
  this.encadrant = encadrant;
 }

 public String getPresident() {
  return president;
 }

 public void setPresident(String president) {
  this.president = president;
 }

 public String getRapporteur() {
  return rapporteur;
 }

 public void setRapporteur(String rapporteur) {
  this.rapporteur = rapporteur;
 }



 public void setDate(String date) {
  this.date_soutenance = date;
 }

 public String getHeure() {
  return heure_soutenance;
 }

 public void setHeure(String heure) {
  this.heure_soutenance = heure;
 }

 public String getSalle() {
  return salle_soutenance;
 }

 public void setSalle(String salle) {
  this.salle_soutenance = salle;
 }




}