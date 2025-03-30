package com.example.Soutenances.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "soutenances")
public class Soutenances {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 @Column(name = "nom_etudiant", nullable = false, length = 255)
 private String nomEtudiant;

 @Column(name = "email", nullable = false, length = 255)
 private String email;

 @Column(name = "titre", nullable = false, length = 255)
 private String titre;

 @Column(name = "encadrant", length = 255)
 private String encadrant;

 @Column(name = "president", length = 255)
 private String president;

 @Column(name = "rapporteur", length = 255)
 private String rapporteur;

 @Column(name = "date_soutenance", nullable = false)
 private String date;  // Utilisation de LocalDate

 @Column(name = "heure", nullable = false)
 private String heure; // Utilisation de LocalTime

 @Column(name = "salle", length = 255)
 private String salle;

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

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public String getTitre() {
  return titre;
 }

 public void setTitre(String titre) {
  this.titre = titre;
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
