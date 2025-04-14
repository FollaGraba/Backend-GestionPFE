package com.example.Soutenances.Services;

import com.example.Soutenances.Entities.*;
import com.example.Soutenances.Repositories.DisponibiliteDateRepository;
import com.example.Soutenances.Repositories.DureeDepartementRepository;
import com.example.Soutenances.Repositories.SallesDisposRepository;
import com.example.Soutenances.Repositories.SoutenancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttribuerService {
    @Autowired
    private  SoutenancesRepository soutenanceRepository;

    @Autowired
    private SallesDisposRepository sallesDispoRepository;

    @Autowired
    private DureeDepartementRepository dureeDepartementRepository;
    @Autowired
    private DisponibiliteDateRepository disponibiliteDateRepository;

 public  String Attribuer(String departement){


    List<Soutenances> soutenancesAPlanifier = soutenanceRepository.findAll().stream()
            .filter(s -> s.getDateSoutenance() == null)
            .filter(s -> s.getNomDept().equalsIgnoreCase(departement))
            .collect(Collectors.toList());

    System.out.println(soutenancesAPlanifier);


     Duree_departement duree = dureeDepartementRepository.findByNomDept(departement);
     LocalDate start = LocalDate.parse(duree.getDate_debut());
     LocalDate end = LocalDate.parse(duree.getDate_fin());
     System.out.println("************start***************");
     System.out.println(start.toString());

     System.out.println(end.toString());
     System.out.println("************fin***************");

     for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
         String jour = date.toString();
         System.out.println(jour);
         List<SallesDispos> sallesDisponibles = sallesDispoRepository.findByDate(jour);
         System.out.println(sallesDisponibles);
         List<String> creneaux = List.of("08:30 - 09:30", "09:45 - 11:15", "11:30 - 12:30", "14:00 - 15:00",  "15:15 - 16:15");


         for (int i = 0; i < creneaux.size(); i++) {
             String creneau = creneaux.get(i);
             System.out.println("Créneau " + (i + 1) + ": " + creneau);
             List<DisponibiliteDate> dispos = disponibiliteDateRepository.findByJourAndSession(jour, creneau);
             System.out.println(dispos);

             Set<Long> profsDisponibles = dispos.stream().map(DisponibiliteDate::getDisponibilite).map(Disponibilite::getIdProfesseur).collect(Collectors.toSet());
             System.out.println(profsDisponibles);

         }



     }
     return "bonjour";
}
}