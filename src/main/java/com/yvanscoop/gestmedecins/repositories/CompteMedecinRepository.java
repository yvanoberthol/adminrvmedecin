package com.yvanscoop.gestmedecins.repositories;


import com.yvanscoop.gestmedecins.entities.CompteMedecin;
import com.yvanscoop.gestmedecins.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface CompteMedecinRepository extends JpaRepository<CompteMedecin,Long> {

    CompteMedecin getCompteMedecinByMedecin(Medecin medecin);
    boolean existsCompteMedecinByLogin(String login);

    @Modifying
    void deleteCompteMedecinByMedecin(Medecin medecin);
}
