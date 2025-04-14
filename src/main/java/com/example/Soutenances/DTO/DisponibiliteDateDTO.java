package com.example.Soutenances.DTO;

import com.example.Soutenances.DTO.DisponibiliteRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@NoArgsConstructor

public class DisponibiliteDateDTO {
    private Long id;
    private String jour;
    private Set<String> sessions;

    @JsonBackReference  // Référence inverse pour éviter la récursion infinie
    private DisponibiliteRequest disponibiliteRequest;  // Ignorer la référence récursive
    public DisponibiliteDateDTO(String jour, Set<String> sessions) {
        this.jour = jour;
        this.sessions = sessions;
    }
    // Getters et Setters
    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public Set<String> getSessions() {
        return sessions;
    }

    public void setSessions(Set<String> sessions) {
        this.sessions = sessions;
    }
}
