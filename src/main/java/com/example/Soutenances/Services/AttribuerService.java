package com.example.Soutenances.Services;


import com.example.Soutenances.Entities.*;
import com.example.Soutenances.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AttribuerService {
    @Autowired
    private SoutenancesRepository soutenanceRepository;

    @Autowired
    private SallesDisposRepository sallesDispoRepository;

    @Autowired
    private DureeDepartementRepository dureeDepartementRepository;

    @Autowired
    private DisponibiliteDateRepository disponibiliteDateRepository;

    @Autowired
    private UserRepository userRepository;

    public String Attribuer(String departement) {

        System.out.println("Soutenances à planifier" );

        List<Soutenances> soutenancesAPlanifier = soutenanceRepository.findAll().stream()
                .filter(s -> s.getDateSoutenance() == null)
                .filter(s -> s.getNomDept().equalsIgnoreCase(departement))
                .collect(Collectors.toList());

        System.out.println("Soutenances à planifier" +soutenancesAPlanifier.toString());


        Duree_departement duree = dureeDepartementRepository.findByNomDept(departement);
        LocalDate start = LocalDate.parse(duree.getDate_debut());
        LocalDate end = LocalDate.parse(duree.getDate_fin());
        System.out.println("************start***************");
        System.out.println(start.toString());

        System.out.println(end.toString());
        System.out.println("************fin***************");

        // Map pour compter le nombre d'affectations de chaque professeur
        Map<String, Integer> compteurAffectations = new HashMap<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            String jour = date.toString();
            List<SallesDispos> sallesDisponibles = sallesDispoRepository.findByDate(jour);
            List<String> creneaux = List.of("08:30 - 09:30", "09:45 - 11:15", "11:30 - 12:30", "14:00 - 15:00", "15:15 - 16:15");

            for (String creneau : creneaux) {
                if (soutenancesAPlanifier.isEmpty()) {
                    break; // Si plus de soutenances à planifier, on sort
                }
                System.out.println("Créneau : " + creneau);
                List<DisponibiliteDate> dispos = disponibiliteDateRepository.findByJourAndSession(jour, creneau);
                System.out.println("les sessions");
                System.out.println(dispos.toString());

                Set<Long> profsDisponibles = dispos.stream()
                        .map(DisponibiliteDate::getDisponibilite)
                        .map(Disponibilite::getIdProfesseur)
                        .collect(Collectors.toSet());

                System.out.println("Les ids des profs dispos");

                System.out.println(profsDisponibles);

                List<User> usersDisponibles = userRepository.findByIdIn(profsDisponibles);

                System.out.println("les noms des profs disponibles");
                List<String> usernamesDisponibles = usersDisponibles.stream()
                        .map(User::getUsername)
                        .collect(Collectors.toList());
                System.out.println(usernamesDisponibles);

                if (!soutenancesAPlanifier.isEmpty()) {
                    Soutenances soutenance = soutenancesAPlanifier.remove(0);



                String encadrant = soutenance.getEncadrant();


                    List<String> candidats = usernamesDisponibles.stream()
                            .filter(username -> !username.equals(encadrant))
                            .filter(username -> compteurAffectations.getOrDefault(username, 0) < 3)
                            .collect(Collectors.toList());

                    if (candidats.size() < 2) {
                        System.out.println("Pas assez de profs disponibles pour la soutenance de : " + soutenance.getNomEtudiant());
                        continue;
                    }

                    // Choix du président et rapporteur
                    String president = candidats.get(0);
                    String rapporteur = candidats.get(1);

                    // Attribution
                    soutenance.setPresident(president);
                    soutenance.setRapporteur(rapporteur);
                    soutenance.setDateSoutenance(jour);
                    soutenance.setHeureSoutenance(creneau);
//                    soutenance.setSalleSoutenance(salle.getNomSalle());

                    // Mise à jour des compteurs
                    compteurAffectations.put(encadrant, compteurAffectations.getOrDefault(encadrant, 0) + 1);
                    compteurAffectations.put(president, compteurAffectations.getOrDefault(president, 0) + 1);
                    compteurAffectations.put(rapporteur, compteurAffectations.getOrDefault(rapporteur, 0) + 1);

                    // Sauvegarde
                    soutenanceRepository.save(soutenance);

                } else {
                    System.out.println("Aucune soutenance à planifier pour cette date.");
                    break;
                }


            }
        }

        return "Attribution terminée.";
    }
}