package com.example.Soutenances.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
public class DisponibiliteRequest {

    private Long idProfesseur;

    private List<DisponibiliteDTO> disponibilites;

    public Long getIdProfesseur() {
        return idProfesseur;
    }

    public void setIdProfesseur(Long idProfesseur) {
        this.idProfesseur = idProfesseur;
    }

    public List<DisponibiliteDTO> getDisponibilites() {
        return disponibilites;
    }

    public void setDisponibilites(List<DisponibiliteDTO> disponibilites) {
        this.disponibilites = disponibilites;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisponibiliteDTO {
        private String jour; // Format : yyyy-MM-dd
        private String session; // Format : HH:mm - HH:mm

        public String getJour() {
            return jour;
        }

        public void setJour(String jour) {
            this.jour = jour;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }
    }
}