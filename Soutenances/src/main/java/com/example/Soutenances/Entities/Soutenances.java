package com.example.Soutenances.Entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "soutenances")
public class Soutenances {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id; // Correction : Changement de int à Long

 @Column(name = "nom_etudiant", columnDefinition = "VARCHAR(255)")
 private String nomEtudiant;

 @Column(name = "email", nullable = false, length = 255)
 private String email;

 @Column(name = "titre_sujet", columnDefinition = "VARCHAR(255)") // Correction : Suppression de l'espace indésirable
 private String titreSujet;

 @Column(name = "encadrant", length = 255)
 private String encadrant;

 @Column(name = "president", length = 255)
 private String president;

 @Column(name = "rapporteur", length = 255)
 private String rapporteur;

 @Column(name = "date_soutenance", columnDefinition = "VARCHAR(255)")
 private String dateSoutenance;

 @Column(name = "heure_soutenance", columnDefinition = "VARCHAR(255)")
 private String heureSoutenance;

 @Column(name = "salle_soutenance", columnDefinition = "VARCHAR(255)")
 private String salleSoutenance;



 @ManyToOne
 @JoinColumn(name = "departement_id", nullable = false)
 private Departement departement;


 public Long getId() {

  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getNomEtudiant() {
  return nomEtudiant;
 }

 public void setNomEtudiant(String nomEtudiant) {
  this.nomEtudiant = nomEtudiant;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public String getTitreSujet() {
  return titreSujet;
 }

 public void setTitreSujet(String titreSujet) {
  this.titreSujet = titreSujet;
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

 public String getSalleSoutenance() {
  return salleSoutenance;
 }

 public void setSalleSoutenance(String salleSoutenance) {
  this.salleSoutenance = salleSoutenance;
 }

 public Departement getDepartement() {
  return departement;
 }


 public void setDepartement(Departement departement) {
  this.departement = departement;
 }

}