package com.yvanscoop.gestmedecins.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yvanscoop.gestmedecins.entities.Specialite;

public interface SpecialiteRepository extends JpaRepository<Specialite,Long> {
	@Query("select s from Specialite s where s.nom = :nom")
	Specialite getSpecialiteByNom(@Param("nom") String nom);
	
	@Query("select s from Specialite s where s.nom like :nom")
	List<Specialite> findByNom(@Param("nom") String nom);
	
	@Query("select s from Specialite s where s.nom like :nom")
	List<Specialite> findByMedecin(@Param("nom") String nom);
}
