package com.yvanscoop.gestmedecins.repositories;

import com.yvanscoop.gestmedecins.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {

    @Query("select m from Medecin m where m.nom like :medecinname or m.prenom like :medecinname")
    List<Medecin> findByMedecin(@Param("medecinname") String medecinname);

    @Query("select m from Medecin m where m.compteMedecin.enabled = 1")
    List<Medecin> findByMedecinActif();

    @Query("select m from Medecin m where m.nom = :medecinname")
    Medecin getByMedecinname(@Param("medecinname") String name);

    Medecin findByMatricule(String matricule);

    Medecin findByEmail(String email);

    @Query(value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'medecin'", nativeQuery = true)
    String[] headers();
}
