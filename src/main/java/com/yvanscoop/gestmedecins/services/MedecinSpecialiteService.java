package com.yvanscoop.gestmedecins.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.entities.MedecinSpecialite;
import com.yvanscoop.gestmedecins.entities.Specialite;
import com.yvanscoop.gestmedecins.repositories.MedecinSpecialiteRepository;

@Service
@Transactional
public class MedecinSpecialiteService {
	
	@Autowired
	private MedecinSpecialiteRepository msRepository;
	
	@Autowired
	private SpecialiteService specialiteService;
	
	@Autowired
	private MedecinService medecinService;
	
	public void deleteSpecialiteFromUser(String specialiteName,String matricule) {
		MedecinSpecialite ms = findMedecinSpecialite(specialiteName, matricule);
		System.out.println(ms.getId());
		delete(ms.getId());
	}
	
	public void delete(Long id) {
		msRepository.delete(id);
	}
	
	public void deleteFromMedecin(Medecin medecin) {
		msRepository.deleteFromMedecin(medecin);
	}
	
	public void deleteFromSpecialite(Specialite specialite) {
		msRepository.deleteFromSpecialite(specialite);
	}
	
	public int countBySpecialite(Specialite specialite) {
		return msRepository.countBySpecialite(specialite);
	}
	
	public MedecinSpecialite findMedecinSpecialite(String specialiteName,String matricule) {
		Specialite specialite = specialiteService.getByNom(specialiteName);
		Medecin medecin = medecinService.getByMatricule(matricule);
		return msRepository.findBySpecialiteMedecin(specialite,medecin);
	}
	
	public List<MedecinSpecialite> findBySpecialite(Specialite specialite) {
		return msRepository.findByDomaine(specialite);
	}

	public void addSpecialiteFromUser(String specialiteName, String matricule) {
		// TODO Auto-generated method stub
		Specialite specialite = specialiteService.getByNom(specialiteName);
		Medecin medecin = medecinService.getByMatricule(matricule);
		 msRepository.save(new MedecinSpecialite(medecin,specialite));
		
	}

}
