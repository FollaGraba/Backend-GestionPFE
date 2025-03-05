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

    public void setDate(String cellValue) {
        this.date = cellValue; // Ensure you are assigning the value
    }

    public void setSalle(String cellValue) {
        this.salle = cellValue; // Ensure you are assigning the value
    }

}
