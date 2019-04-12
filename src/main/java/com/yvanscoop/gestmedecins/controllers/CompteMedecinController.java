package com.yvanscoop.gestmedecins.controllers;


import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.services.CompteMedecinService;
import com.yvanscoop.gestmedecins.services.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/compte")
public class CompteMedecinController {

    @Autowired
    private CompteMedecinService compteMedecinService;

    @Autowired
    private MedecinService medecinService;

    @RequestMapping(value = "/desactiveMedecin", method = RequestMethod.POST)
    public String desactiveCompte(@RequestParam("id") Long id) {

        Medecin medecin = medecinService.getOne(id);
        compteMedecinService.desactivateCompte(medecin);

        return "redirect:/medecins";
    }

    @RequestMapping(value = "/activeMedecin", method = RequestMethod.POST)
    public String activeCompte(@RequestParam("id") Long id) {

        Medecin medecin = medecinService.getOne(id);
        compteMedecinService.activateCompte(medecin);

        return "redirect:/medecins";
    }

}
