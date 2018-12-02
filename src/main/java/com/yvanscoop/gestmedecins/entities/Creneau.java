package com.yvanscoop.gestmedecins.entities;

import javax.persistence.*;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "creneaux")
public class Creneau {

	@Id @GeneratedValue
	private Long id;
	
	@Range(min=7,max=18,message="7 à 18")
    private int hdebut;
	
	@Range(min=0,max=59,message="0 à 59")
    private int mdebut;
    
	@Range(min=7,max=18,message="7 à 18")
    private int hfin;
    
	@Range(min=0,max=59,message="0 à 59")
    private int mfin;

    //creneau lié au medecin
    @ManyToOne
    @JoinColumn(name = "id_medecin")
    private Medecin medecin;
    
    public Creneau() {
		// TODO Auto-generated constructor stub
	}


    public Creneau(int hdebut, int mdebut, int hfin, int mfin, Medecin medecin){
        this.medecin = medecin;
        this.hdebut = hdebut;
        this.mdebut = mdebut;
        this.hfin = hfin;
        this.mfin = mfin;
    }

    

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public int getHdebut() {
		return hdebut;
	}


	public void setHdebut(int hdebut) {
		this.hdebut = hdebut;
	}


	public int getMdebut() {
		return mdebut;
	}


	public void setMdebut(int mdebut) {
		this.mdebut = mdebut;
	}


	public int getHfin() {
		return hfin;
	}


	public void setHfin(int hfin) {
		this.hfin = hfin;
	}


	public int getMfin() {
		return mfin;
	}


	public void setMfin(int mfin) {
		this.mfin = mfin;
	}


	public Medecin getMedecin() {
		return medecin;
	}


	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}
    
    

}
