package com.yvanscoop.gestmedecins.repositories;

import com.yvanscoop.gestmedecins.entities.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreneauRepository extends JpaRepository<Creneau, Long> {

    // avoir la liste de creneaux par médecin
    @Query("select c from Creneau c where c.medecin in (select m from Medecin m where m.matricule like ?1) order by c.medecin,c.hdebut,c.mdebut")
    List<Creneau> findWithMedecin(String matricule);

    //rechercher le creneau par son heure de debut
    @Query("select c from Creneau c where c.hdebut = ?1 and c.mdebut = ?2 and c.medecin in (select m from Medecin m where m.matricule = ?3)")
    Creneau findHdebutByMedecin(int hDebut, int mDebut, String matricule);

    // rechercher le creneau par son heure de fin
    @Query("select c from Creneau c where c.hfin = ?1 and c.mfin = ?2 and c.medecin in (select m from Medecin m where m.matricule = ?3)")
    Creneau findHfinByMedecin(int hFin, int mFin, String matricule);


    /* le mystere de l'intervalle */

    @Query("select c from Creneau c where c.hdebut <= ?1 and c.mdebut < ?2 and c.hfin <= ?1 and c.medecin in (select m from Medecin m where m.matricule = ?3)")
    List<Creneau> findHDMsupouegalHDA(int hdebutEntree, int mdebutEntree, String matricule);


    @Query("select c from Creneau c where c.hdebut >= ?1 and c.hfin >= ?1 and c.medecin in (select m from Medecin m where m.matricule = ?2)")
    List<Creneau> findHFMinfouegalHDAP(int hfinEntree, String matricule);
}
