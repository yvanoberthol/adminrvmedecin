package com.yvanscoop.gestmedecins.controllers;

import com.itextpdf.text.DocumentException;
import com.yvanscoop.gestmedecins.entities.CompteMedecin;
import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.entities.MedecinSpecialite;
import com.yvanscoop.gestmedecins.entities.Specialite;
import com.yvanscoop.gestmedecins.repositories.MedecinSpecialiteRepository;
import com.yvanscoop.gestmedecins.services.CompteMedecinService;
import com.yvanscoop.gestmedecins.services.MedecinService;
import com.yvanscoop.gestmedecins.services.MedecinSpecialiteService;
import com.yvanscoop.gestmedecins.services.SpecialiteService;
import com.yvanscoop.gestmedecins.services.mail.MailGlobal;
import com.yvanscoop.gestmedecins.services.pdf.ItextInPdf;
import com.yvanscoop.gestmedecins.utilities.SecurityUtility;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MedecinController {
    private static int currentPage = 1;
    private static int pageSize = 5;
    private static String motsearch = "";

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private SpecialiteService specialiteService;

    @Autowired
    private CompteMedecinService compteMedecinService;

    @Autowired
    private MedecinSpecialiteRepository msRepository;

    @Autowired
    private MedecinSpecialiteService msService;

    @Autowired
    private MailGlobal mailConfig;

    @RequestMapping(value = "/medecins")
    public String getAllMedecins(Model model,
                                 @RequestParam("nom") Optional<String> motcle,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        motcle.ifPresent(nom -> motsearch = nom);
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);
        Page<Medecin> medecinPage = medecinService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));

        int totalPages = medecinPage.getTotalPages();
        if (currentPage > totalPages) {
            currentPage = 1;
            medecinPage = medecinService.findPaginated(motsearch, PageRequest.of(currentPage - 1, pageSize));
        }
        model.addAttribute("medecins", medecinPage);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("medecinActifCount",medecinService.countActif());
        return "medecins";
    }

    @RequestMapping(value = "/deleteMedecin", method = RequestMethod.POST)
    @Transactional
    public String deleteMedecin(@RequestParam("id") Long id) {
        Medecin medecin = medecinService.getOne(id);
        if (medecin != null) {
            medecinService.delete(id);
            //we delete the picture in his name

            dropImage(medecin);

            //delete all entries of this medecin in table medecinSpecialite
            msService.deleteFromMedecin(medecin);
            compteMedecinService.deleteCompte(medecin);
        }

        return "redirect:/medecins";
    }

    @RequestMapping(value = "/formModifMedecin")
    public String formModifMedecin(@RequestParam("id") Long id, Model model) {
        Medecin medecin = medecinService.getOne(id);
        model.addAttribute("medecin", medecin);
        return "modifMedecin";
    }

    @RequestMapping(value = "/detailMedecin")
    public String detailMedecin(@RequestParam("id") Long id, Model model) {
        Medecin medecin = medecinService.getOne(id);
        List<MedecinSpecialite> lms = msRepository.findByMedecin(medecin);
        List<Specialite> specialiteParticulieres = new ArrayList<>();
        for (MedecinSpecialite ms : lms) {
            specialiteParticulieres.add(ms.getSpecialite());
        }
        model.addAttribute("medecin", medecin);
        model.addAttribute("specialiteParticulieres", specialiteParticulieres);
        model.addAttribute("specialites", specialiteService.getAll(""));
        return "detailMedecin";
    }

    @RequestMapping(value = "/formAddMedecin")
    public String formAddMedecin(Model model) {
        model.addAttribute("medecin", new Medecin());
        model.addAttribute("specialites", specialiteService.getAll(""));
        return "addMedecin";
    }

    @RequestMapping(value = "/modifMedecin", method = RequestMethod.POST)
    @Transactional
    public String modifMedecin(@Valid Medecin medecinUpdated, BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            /* we research medecin's informations */
            Medecin medecin = medecinService.getOne(medecinUpdated.getId());

            //vérification sur l'existence d'un médecin par son matricule
            Medecin medecinBD = medecinService.getByMatricule(medecinUpdated.getMatricule());
            if (medecinBD != null) {
                if (!medecinBD.getMatricule().equals(medecin.getMatricule())
                        || !medecinBD.getEmail().equals(medecinUpdated.getEmail())) {
                    model.addAttribute("medecinExists", true);
                    model.addAttribute("medecin", medecin);
                    return "modifMedecin";
                }
            }

            // sauver les noms pour la photo
            String lastNom = medecin.getNom();
            String lastPrenom = medecin.getPrenom();

            //médécin sauvegardé
            Medecin updatedMedecin = setMedecin(medecin,medecinUpdated);

            // creation du compte
            if(updatedMedecin != null){
                creerCompteMedecin(updatedMedecin);
            }

            // we describe the process of medecin's image's registration
            /* we take medecin's image */
            MultipartFile updateMedecinImage = medecinUpdated.getPhoto();

            /* we verify if he is empty */
            if (updateMedecinImage.isEmpty()) {
                // process to rename picture's medecin
                String oldName = medecin.getId() + "_" + lastNom + "_" + lastPrenom + ".png";
                /* old path image */
                String oldpathImage = "src/main/resources/static/images/medecins/" + oldName;
                File photo = new File(oldpathImage);

                /* image name */
                String newName = medecinUpdated.getId() + "_" + medecinUpdated.getNom() + "_" + medecinUpdated.getPrenom() + ".png";

                /* new path image */
                String newpathImage = "src/main/resources/static/images/medecins/" + newName;
                File newPhotoName = new File(newpathImage);

                //renommer la photo
                photo.renameTo(newPhotoName);

            }else{
                dropImage(medecin);
                saveImage(medecinUpdated);

            }
            return "redirect:/medecins";
        } else {
            return "modifMedecin";
        }

    }

    @RequestMapping(value = "/addMedecin", method = RequestMethod.POST)
    @Transactional
    public String addMedecin(@Valid Medecin medecinadd, BindingResult bindingResult, @RequestParam("specialites") List<String> specialites, Model model) {

        if (!bindingResult.hasErrors() && !specialites.isEmpty() && !medecinadd.getPhoto().isEmpty()) {
            //vérification sur l'existence d'un médecin par son matricule
            Medecin medecinMatricule = medecinService.getByMatricule(medecinadd.getMatricule());
            if (medecinMatricule != null) {
                model.addAttribute("medecinExists", true);
                model.addAttribute("medecin", medecinadd);
                return "addMedecin";
            }

            //vérification sur l'existence d'un médecin par son email
            Medecin medecinEmail = medecinService.getByEmail(medecinadd.getEmail());
            if (medecinEmail != null) {
                if (!medecinEmail.getEmail().equals(medecinadd.getEmail())) {
                    model.addAttribute("medecinExists", true);
                    model.addAttribute("medecin", medecinadd);
                    return "addMedecin";
                }
            }

            // sauvegarder le médécin
            Medecin medecinAdded = medecinService.save(medecinadd);

            // creation du compte
            if (medecinAdded != null){

                //creer un compte au médécin
                creerCompteMedecin(medecinAdded);

                //ajout des spécialités au médécin
                for (String specialiteName : specialites) {
                    Specialite specialite = specialiteService.getByNom(specialiteName);
                    if (specialite != null) {
                        addMedecinSpecialite(medecinadd, specialite);
                    }
                }

                //sauvegarder la photo du médécin
                saveImage(medecinadd);

            }

            //redirect in the medecin list
            return "redirect:/medecins";
        } else {

            System.out.println(specialites);
            if (specialites.isEmpty()) {
                model.addAttribute("medecinSpecialitesEmpty", true);
            }

            if (medecinadd.getPhoto().isEmpty()) {
                model.addAttribute("medecinPhotoEmpty", true);
            }

            model.addAttribute("specialites", specialiteService.getAll(""));
            return "addMedecin";
        }

    }

    @PostMapping("/changerPhoto")
    private String changerPhoto(@RequestParam(name = "id") Long id,@RequestParam(name = "photo") MultipartFile photo){
      System.out.print("medecin_Long_id: "+id);

      Medecin medecin = medecinService.getOne(id);
      medecin.setPhoto(photo);
      saveImage(medecin);
      return "redirect:/detailMedecin?id="+id;
    }

    @GetMapping("/medecin/imprimer")
    private String imprimer(RedirectAttributes redirectAttributes) throws DocumentException, IOException {

        List<Medecin> medecinList = medecinService.getAll("");

        String[] headers = {"Photo","Matricule","Nom","Prénom"};
        int nbre_colonnes = headers.length;

        Random rand = new Random();

        // Obtain a number between [10 - 20].
        int n = rand.nextInt(10) + 10;
        String name = RandomStringUtils.randomAlphanumeric(n);

        ItextInPdf pdf = new ItextInPdf(name);
        pdf.setNbre_colonnes(nbre_colonnes);
        pdf.setEntetes(headers);
        pdf.setMedecins(medecinList);
        pdf.imprimerMedecin();
        redirectAttributes.addFlashAttribute("impressionDone",true);

        return "redirect:/medecins";
    }


    private void addMedecinSpecialite(Medecin medecin, Specialite specialite) {
        msRepository.save(new MedecinSpecialite(medecin, specialite));
    }


    private void saveImage(Medecin medecinadd){
        // we describe the process of medecin's image's registration
        /* we take medecin's image */
        MultipartFile addMedecinImage = medecinadd.getPhoto();
        /* we verify if he is empty */
        if (!addMedecinImage.isEmpty()) {
            try {
                byte[] bytes = addMedecinImage.getBytes();

                /* image name */
                String name = medecinadd.getId() + "_" + medecinadd.getNom() + "_" + medecinadd.getPrenom() + ".png";

                /* path image */
                String pathImage = "src/main/resources/static/images/medecins/" + name;

                /* delete old file */
                Files.deleteIfExists(Paths.get(pathImage));

                // save file
                /* open buffer in out */
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(pathImage)
                );
                /* write in the buffer */
                stream.write(bytes);
                /* close buffer */
                stream.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private void dropImage(Medecin medecin){
        try {

            /* image name */
            String name = medecin.getId() + "_" + medecin.getNom() + "_" + medecin.getPrenom() + ".png";

            /* path image */
            String pathImage = "src/main/resources/static/images/medecins/" + name;

            /* delete file */
            Files.deleteIfExists(Paths.get(pathImage));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void creerCompteMedecin(Medecin medecin){
        CompteMedecin compteMedecin = compteMedecinService.getByMedecin(medecin);
        if (!compteMedecinService.loginExist(compteMedecin.getLogin())) {
            CompteMedecin compteMedecin1 = new CompteMedecin();
            compteMedecin1.setLogin(medecin.getMatricule());
            String randomPassword = SecurityUtility.randomPassword(18);
            compteMedecin1.setPassword(randomPassword);
            CompteMedecin compteCreated = compteMedecinService.addCompte(compteMedecin1);

            if (compteCreated != null) {
                //envoi du mot de passe au médécin
                String msgSended = "\nYour password is: " + randomPassword;
                String subject = "Mon cabinet médical: Your account password";
                try {
                    mailConfig.sendMail(medecin.getEmail(), subject, msgSended);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private Medecin setMedecin(Medecin medecin,Medecin medecinUpdated){
        /* we modify his informations */
        medecin.setMatricule(medecinUpdated.getMatricule());
        medecin.setNom(medecinUpdated.getNom());
        medecin.setPrenom(medecinUpdated.getPrenom());
        medecin.setEmail(medecinUpdated.getEmail());
        medecin.setDateNaissance(medecinUpdated.getDateNaissance());
        medecin.setTelephone(medecinUpdated.getTelephone());
        medecin.setVille(medecinUpdated.getVille());
        /* after modification we save this medecin */
        return medecinService.save(medecin);
    }
}
