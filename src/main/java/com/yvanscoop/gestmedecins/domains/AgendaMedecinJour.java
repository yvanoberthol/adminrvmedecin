package com.yvanscoop.gestmedecins.domains;

import java.util.Date;

import com.yvanscoop.gestmedecins.entities.Medecin;

public class AgendaMedecinJour {

	// champs
	private Medecin medecin;
	private Date jour;
	private CreneauMedecinJour[] creneauxMedecinJour;

	// constructeurs
	public AgendaMedecinJour() {

	}

	public AgendaMedecinJour(Medecin medecin, Date jour, CreneauMedecinJour[] creneauxMedecinJour) {
		this.medecin = medecin;
		this.jour = jour;
		this.creneauxMedecinJour = creneauxMedecinJour;
	}


	// getters et setters

  public CreneauMedecinJour[] getCreneauxMedecinJour() {
    return creneauxMedecinJour;
  }

  public void setCreneauxMedecinJour(CreneauMedecinJour[] creneauxMedecinJour) {
    this.creneauxMedecinJour = creneauxMedecinJour;
  }

  public Date getJour() {
    return jour;
  }

  public void setJour(Date jour) {
    this.jour = jour;
  }

  public Medecin getMedecin() {
    return medecin;
  }

  public void setMedecin(Medecin medecin) {
    this.medecin = medecin;
  }
  
}
