package com.yvanscoop.gestmedecins.controllers.restcontrollers;


import com.yvanscoop.gestmedecins.entities.Rv;
import com.yvanscoop.gestmedecins.services.RvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rv")
public class RvPatient {

    @Autowired
    RvService rvService;


    @RequestMapping("/patient/{email}")
    public List<String> apiRvPatient(@PathVariable(name = "email") String email){

        List<Rv> rvList = rvService.getRvByPatient(email);
        List<String> rvsPatient = new ArrayList<>();

        for (Rv rv : rvList) {

            rvsPatient.add(rv.getCreneau().getCreneauAll()+" "+rv.getJour()+" "+rv.getSpecialite().getNom());
        }

        return rvsPatient;
    }
}
