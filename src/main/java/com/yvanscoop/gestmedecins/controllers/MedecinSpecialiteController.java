package com.yvanscoop.gestmedecins.controllers;

import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.entities.MedecinSpecialite;
import com.yvanscoop.gestmedecins.services.MedecinService;
import com.yvanscoop.gestmedecins.services.MedecinSpecialiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MedecinSpecialiteController {
    @Autowired
    private MedecinService medecinService;


    @Autowired
    private MedecinSpecialiteService msService;

    @RequestMapping(value = "/deleteMedecinSpecialite", method = RequestMethod.POST)
    public String deleteMedecinSpecialite(String specialiteName, String matricule, RedirectAttributes redirectAttribute) {
        Medecin medecin = medecinService.getByMatricule(matricule);
        msService.deleteSpecialiteFromUser(specialiteName, matricule);
        redirectAttribute.addFlashAttribute("competenceDelete", true);
        return "redirect:/detailMedecin?id=" + medecin.getId();
    }

    @RequestMapping(value = "/addMedecinSpecialite", method = RequestMethod.POST)
    public String addMedecinSpecialite(String specialiteName, String matricule, RedirectAttributes redirectAttribute) {
        Medecin medecin = medecinService.getByMatricule(matricule);
        MedecinSpecialite ms = msService.findMedecinSpecialite(specialiteName, matricule);
        if (ms != null) {
            redirectAttribute.addFlashAttribute("competenceExists", true);
            return "redirect:/detailMedecin?id=" + medecin.getId();
        } else {
            msService.addSpecialiteFromUser(specialiteName, matricule);
            redirectAttribute.addFlashAttribute("competenceAdd", true);
            return "redirect:/detailMedecin?id=" + medecin.getId();
        }
    }

}
