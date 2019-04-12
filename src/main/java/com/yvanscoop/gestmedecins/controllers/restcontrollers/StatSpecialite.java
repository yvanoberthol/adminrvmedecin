package com.yvanscoop.gestmedecins.controllers.restcontrollers;


import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.entities.MedecinSpecialite;
import com.yvanscoop.gestmedecins.entities.Specialite;
import com.yvanscoop.gestmedecins.services.MedecinService;
import com.yvanscoop.gestmedecins.services.MedecinSpecialiteService;
import com.yvanscoop.gestmedecins.services.SpecialiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stat")
public class StatSpecialite {

    @Autowired
    private MedecinSpecialiteService medecinSpecialiteService;

    @Autowired
    private SpecialiteService specialiteService;

    @Autowired
    private MedecinService medecinService;

    @RequestMapping("/specialiteMedecin/{specialiteName}")
    public List<String> apiStatSpecialite(@PathVariable(name = "specialiteName") String specialiteName){
        Specialite specialite = specialiteService.getByNom(specialiteName);

        List<MedecinSpecialite> medecinSpecialites = medecinSpecialiteService.findBySpecialite(specialite);
        List<String> medecins = new ArrayList<>();

        for (MedecinSpecialite medecinSpecialite : medecinSpecialites) {
            Medecin medecin = medecinSpecialite.getMedecin();
            medecins.add(medecin.getId()+" "+medecin.getNom()+" "+medecin.getPrenom());
        }

        return medecins;
    }
}
