package com.yvanscoop.gestmedecins.services;


import com.yvanscoop.gestmedecins.entities.CompteMedecin;
import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.repositories.CompteMedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompteMedecinService {

    @Autowired
    private CompteMedecinRepository compteMedecinRepository;

    public CompteMedecin addCompte(CompteMedecin cmpte) {
        return compteMedecinRepository.save(cmpte);
    }

    public void desactivateCompte(Medecin medecin) {
        CompteMedecin cmpteMedecin = medecin.getCompteMedecin();
        cmpteMedecin.setEnabled(false);
        compteMedecinRepository.save(cmpteMedecin);
    }

    public void activateCompte(Medecin medecin) {
        CompteMedecin cmpteMedecin = medecin.getCompteMedecin();
        cmpteMedecin.setEnabled(true);
        compteMedecinRepository.save(cmpteMedecin);
    }

    public boolean loginExist(String login) {
        return compteMedecinRepository.existsCompteMedecinByLogin(login);
    }

    public CompteMedecin getByMedecin(Medecin medecin) {
        return compteMedecinRepository.getCompteMedecinByMedecin(medecin);
    }

    public void deleteCompte(Medecin medecin) {
        compteMedecinRepository.deleteCompteMedecinByMedecin(medecin);
    }
}
