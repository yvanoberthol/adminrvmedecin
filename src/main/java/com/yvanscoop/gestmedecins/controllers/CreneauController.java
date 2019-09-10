package com.yvanscoop.gestmedecins.controllers;

import com.yvanscoop.gestmedecins.entities.Creneau;
import com.yvanscoop.gestmedecins.services.CreneauService;
import com.yvanscoop.gestmedecins.services.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CreneauController {

    private static final int intervalleCreneau = 20;
    private static int currentPage = 1;
    private static int pageSize = 5;
    private static String motsearch = "";
    @Autowired
    private CreneauService creneauService;
    @Autowired
    private MedecinService medecinService;

    @RequestMapping(value = "/formAddCreneau", method = RequestMethod.GET)
    public String formAddCreneau(Model model) {
        model.addAttribute("creneau", new Creneau());
        model.addAttribute("medecins", medecinService.getAll(""));
        return "addCreneau";
    }

    @RequestMapping(value = "/formModifCreneau", method = RequestMethod.GET)
    public String formModifCreneau(@RequestParam(name = "id") Long idCreneau, Model model) {
        model.addAttribute("creneau", creneauService.getCrenauById(idCreneau));
        model.addAttribute("medecins", medecinService.getAll(""));
        return "modifCreneau";
    }

    @RequestMapping(value = "/deleteCreneau", method = RequestMethod.POST)
    public String deleteCreneau(@RequestParam("id") Long id) {
        Creneau creneau = creneauService.getCrenauById(id);
        if (creneau != null) {
            creneauService.delete(id);
        }
        return "redirect:/creneaux";
    }

    @RequestMapping(value = "/addCreneau", method = RequestMethod.POST)
    public String addCreneau(@Valid Creneau creneauAdd, BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            Creneau creneauByHdebutMedecinCreneau =
                    creneauService.getCreneauByHdebutMedecin(creneauAdd.getHdebut(), creneauAdd.getMdebut(), creneauAdd.getMedecin().getMatricule());


            Creneau creneauByHfinMedecinCreneau =
                    creneauService.getCreneauByHfinMedecin(creneauAdd.getHfin(), creneauAdd.getMfin(), creneauAdd.getMedecin().getMatricule());

            List<Creneau> creneauHDMsupouegalHDAV = creneauService.findHDMsupouegalHDA(creneauAdd.getHdebut(), creneauAdd.getMdebut(), creneauAdd.getMedecin().getMatricule());

            List<Creneau> creneauHFMinfouegalHDAP = creneauService.findHFMinfouegalHDAP(creneauAdd.getHfin(), creneauAdd.getMedecin().getMatricule());

            //on verifie s'il y'a deja ce debut de creneau pour ce médécin
            if (creneauByHdebutMedecinCreneau != null) {
                model.addAttribute("hdebutExists", true);
                model.addAttribute("creneau", creneauAdd);
                model.addAttribute("medecins", medecinService.getAll(""));
                return "addCreneau";
            }

            //on verifie s'il y'a deja cette fin de creneau pour ce médécin
            if (creneauByHfinMedecinCreneau != null) {
                model.addAttribute("hfinExists", true);
                model.addAttribute("creneau", creneauAdd);
                model.addAttribute("medecins", medecinService.getAll(""));
                return "modifCreneau";
            }

            System.out.println("dans add creneau 1:" + creneauHDMsupouegalHDAV.size());
            //on verifie des créneaux avec un debut inferieur à celui de ce médecin
            if (!creneauHDMsupouegalHDAV.isEmpty()) {
                for (Creneau creneau2 : creneauHDMsupouegalHDAV) {
                    if (creneauAdd.getHdebut() == creneau2.getHfin()) {
                        if (creneauAdd.getMdebut() <= creneau2.getMfin()) {
                            model.addAttribute("hfinLesshdebut", true);
                            model.addAttribute("creneau", creneauAdd);
                            model.addAttribute("medecins", medecinService.getAll(""));
                            return "addCreneau";
                        }
                    }
                }
            }

            System.out.println("dans add creneau 2:" + creneauHFMinfouegalHDAP.size());
            //on verifie des créneaux avec un debut inferieur à celui de ce médecin
            if (!creneauHFMinfouegalHDAP.isEmpty()) {
                for (Creneau creneau3 : creneauHFMinfouegalHDAP) {
                    if (creneauAdd.getHfin() == creneau3.getHdebut()) {
                        if (creneauAdd.getMfin() >= creneau3.getMdebut() && !creneauHDMsupouegalHDAV.contains(creneau3)) {
                            model.addAttribute("hfinLesshdebut", true);
                            model.addAttribute("creneau", creneauAdd);
                            model.addAttribute("medecins", medecinService.getAll(""));
                            return "addCreneau";
                        }
                    }
                }
            }


            // on verifie certaines conditions avant la sauvegarde du creneau
            if (creneauAdd.getHdebut() < creneauAdd.getHfin()) {
                if (creneauAdd.getHdebut() + 1 < creneauAdd.getHfin()) {
                    creneauService.save(creneauAdd);
                } else if (creneauAdd.getHdebut() + 1 == creneauAdd.getHfin()) {
                    int mfin = creneauAdd.getMfin() + 60;
                    int intervalle = mfin - creneauAdd.getMdebut();
                    if (intervalle >= 20) {
                        creneauService.save(creneauAdd);
                    } else {
                        model.addAttribute("dureeIntervalle", true);
                        model.addAttribute("creneau", creneauAdd);
                        model.addAttribute("medecins", medecinService.getAll(""));
                        return "addCreneau";
                    }
                } else {
                    model.addAttribute("dureeIntervalle", true);
                    model.addAttribute("creneau", creneauAdd);
                    model.addAttribute("medecins", medecinService.getAll(""));
                    return "addCreneau";
                }
            } else if (creneauAdd.getHdebut() == creneauAdd.getHfin()) {
                if ((creneauAdd.getMdebut() + intervalleCreneau) <= creneauAdd.getMfin()) {
                    creneauService.save(creneauAdd);
                } else if (creneauAdd.getMdebut() > creneauAdd.getMfin()) {
                    model.addAttribute("hdebutGrand", true);
                    model.addAttribute("creneau", creneauAdd);
                    model.addAttribute("medecins", medecinService.getAll(""));
                    return "addCreneau";
                } else {
                    model.addAttribute("dureeIntervalle", true);
                    model.addAttribute("creneau", creneauAdd);
                    model.addAttribute("medecins", medecinService.getAll(""));
                    return "addCreneau";
                }
            } else {
                model.addAttribute("hdebutGrand", true);
                model.addAttribute("creneau", creneauAdd);
                model.addAttribute("medecins", medecinService.getAll(""));
                return "addCreneau";
            }


        } else {
            model.addAttribute("medecins", medecinService.getAll(""));
            return "addCreneau";
        }
        return "redirect:/creneaux";

    }

    @RequestMapping(value = "/modifCreneau", method = RequestMethod.POST)
    public String modifCreneau(@Valid Creneau creneauUpdated, BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            Creneau creneau = creneauService.getCrenauById(creneauUpdated.getId());


            Creneau creneauByHdebutMedecinCreneau =
                    creneauService.getCreneauByHdebutMedecin(creneauUpdated.getHdebut(), creneauUpdated.getMdebut(), creneauUpdated.getMedecin().getMatricule());

            Creneau creneauByHfinMedecinCreneau =
                    creneauService.getCreneauByHfinMedecin(creneauUpdated.getHfin(), creneauUpdated.getMfin(), creneauUpdated.getMedecin().getMatricule());

            List<Creneau> creneauHDMsupouegalHDAV = creneauService.findHDMsupouegalHDA(creneauUpdated.getHdebut(), creneauUpdated.getMdebut(), creneauUpdated.getMedecin().getMatricule());

            List<Creneau> creneauHFMinfouegalHDAP = creneauService.findHFMinfouegalHDAP(creneauUpdated.getHfin(), creneauUpdated.getMedecin().getMatricule());


            //on verifie s'il y'a deja ce debut de creneau pour ce médécin
            if (creneauByHdebutMedecinCreneau != null) {
                if (!creneauByHdebutMedecinCreneau.getId().equals(creneau.getId())) {
                    model.addAttribute("hdebutExists", true);
                    model.addAttribute("creneau", creneauUpdated);
                    model.addAttribute("medecins", medecinService.getAll(""));
                    return "modifCreneau";
                }
            }

            //on verifie s'il y'a deja cette fin de creneau pour ce médécin
            if (creneauByHfinMedecinCreneau != null) {
                if (!creneauByHfinMedecinCreneau.getId().equals(creneau.getId())) {
                    model.addAttribute("hfinExists", true);
                    model.addAttribute("creneau", creneauUpdated);
                    model.addAttribute("medecins", medecinService.getAll(""));
                    return "modifCreneau";
                }
            }


            System.out.println("dans modif creneau 1:" + creneauHDMsupouegalHDAV.size());
            //on verifie des créneaux avec un debut inferieur à celui de ce médecin
            if (!creneauHDMsupouegalHDAV.isEmpty()) {
                for (Creneau creneau2 : creneauHDMsupouegalHDAV) {
                    if (!creneau2.getId().equals(creneau.getId())) {
                        if (creneauUpdated.getHdebut() == creneau2.getHfin()) {
                            if (creneauUpdated.getMdebut() <= creneau2.getMfin()) {
                                model.addAttribute("hfinLesshdebut", true);
                                model.addAttribute("creneau", creneauUpdated);
                                model.addAttribute("medecins", medecinService.getAll(""));
                                return "modifCreneau";
                            }
                        }
                    }
                }
            }

            //on verifie des créneaux avec un debut inferieur à celui de ce médecin
            if (!creneauHFMinfouegalHDAP.isEmpty()) {
                for (Creneau creneau3 : creneauHFMinfouegalHDAP) {
                    System.out.println("dans modif creneau 2:" + creneau3.getId());
                    if (!creneau3.getId().equals(creneau.getId())) {
                        if (creneauUpdated.getHfin() == creneau3.getHdebut()) {
                            if (creneauUpdated.getMfin() >= creneau3.getMdebut() && !creneauHDMsupouegalHDAV.contains(creneau3)) {
                                model.addAttribute("hfinLesshdebut", true);
                                model.addAttribute("creneau", creneauUpdated);
                                model.addAttribute("medecins", medecinService.getAll(""));
                                return "modifCreneau";
                            }
                        }
                    }
                }
            }


            // on verifie certaines conditions avant la sauvegarde du creneau
            if (creneauUpdated.getHdebut() < creneauUpdated.getHfin()) {
                    save(creneau,creneauUpdated);
            } else if (creneauUpdated.getHdebut() == creneauUpdated.getHfin()) {
                if ((creneauUpdated.getMdebut() + intervalleCreneau) <= creneauUpdated.getMfin()) {
                    save(creneau,creneauUpdated);
                } else if (creneauUpdated.getMdebut() > creneauUpdated.getMfin()) {
                    model.addAttribute("hdebutGrand", true);
                    model.addAttribute("creneau", creneauUpdated);
                    model.addAttribute("medecins", medecinService.getAll(""));
                    return "modifCreneau";
                } else {
                    model.addAttribute("dureeIntervalle", true);
                    model.addAttribute("creneau", creneauUpdated);
                    model.addAttribute("medecins", medecinService.getAll(""));
                    return "modifCreneau";
                }
            } else {
                model.addAttribute("hdebutGrand", true);
                model.addAttribute("creneau", creneauUpdated);
                model.addAttribute("medecins", medecinService.getAll(""));
                return "modifCreneau";
            }

        } else {
            model.addAttribute("medecins", medecinService.getAll(""));
            return "modifCreneau";
        }
        return "redirect:/creneaux";

    }

    @RequestMapping(value = "/creneaux", method = RequestMethod.GET)
    public String listByMedecin(
            @RequestParam(name = "matricule") Optional<String> matricule,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model) {
        matricule.ifPresent(nom -> motsearch = nom);
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        Page<Creneau> creneauPage = creneauService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));

        int totalPages = creneauPage.getTotalPages();
        if (currentPage > totalPages) {
            currentPage = 1;
            creneauPage = creneauService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));
        }
        model.addAttribute("creneaux", creneauPage);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("motcle", motsearch);
        model.addAttribute("medecins", medecinService.getAll(""));

        return "creneaux";
    }

    private void save(Creneau creneau,Creneau modif){
        creneau.setHdebut(modif.getHdebut());
        creneau.setHfin(modif.getHfin());
        creneau.setMdebut(modif.getMdebut());
        creneau.setMfin(modif.getMfin());
        creneau.setMedecin(modif.getMedecin());
        creneauService.save(creneau);
    }
}
