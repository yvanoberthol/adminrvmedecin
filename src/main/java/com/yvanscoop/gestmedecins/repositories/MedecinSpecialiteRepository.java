package com.yvanscoop.gestmedecins.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.entities.MedecinSpecialite;
import com.yvanscoop.gestmedecins.entities.Specialite;

public interface MedecinSpecialiteRepository extends JpaRepository<MedecinSpecialite,Long> {

	
	@Query("select ms from MedecinSpecialite ms where ms.specialite = :specialite and ms.medecin = :medecin")
	MedecinSpecialite findBySpecialiteMedecin(@Param("specialite") Specialite specialite,@Param("medecin") Medecin medecin);
	
	List<MedecinSpecialite> findByMedecin(Medecin medecin);
	
	@Modifying
	@Transactional
	@Query("delete from MedecinSpecialite ms where ms.id = ?1")
	void delete(Long id);
	
	@Modifying
	@Transactional
	@Query("delete from MedecinSpecialite ms where ms.medecin = :medecin")
	void deleteFromMedecin(@Param("medecin") Medecin medecin);
	
	@Modifying
	@Transactional
	@Query("delete from MedecinSpecialite ms where ms.specialite = :specialite")
	void deleteFromSpecialite(@Param("specialite") Specialite specialite);
	
	@Query("select ms from MedecinSpecialite ms where ms.specialite = :specialite")
	List<MedecinSpecialite> findByDomaine(@Param("specialite") Specialite specialite);
	
	@Query("select count(ms) from MedecinSpecialite ms where ms.specialite = ?1")
	int countBySpecialite(Specialite specialite);
}
