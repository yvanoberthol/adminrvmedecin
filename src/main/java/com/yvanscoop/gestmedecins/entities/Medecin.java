package com.yvanscoop.gestmedecins.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class Medecin{
	@Id @GeneratedValue
	private Long id;
	
	@NotEmpty(message="entrez un matricule pour ce médécin.")
	@Length(min=4, message="le matricule doit compter au moins 4 caractères.")
	@Column(nullable=false)
	private String matricule;
	
	@NotEmpty(message="entrez un nom pour ce médécin.")
	@Length(min=4, message="le nom du médécin doit compter au moins 4 caractères.")
	@Column(nullable=false)
	private String nom;
	
	@NotEmpty(message="entrez un prénom pour ce médécin.")
	@Length(min=4, message="le prénom du médécin doit compter au moins 4 caractères.")
	@Column(nullable=false)
	private String prenom;
	
	@Past(message="la date de naissance doit être antérieure à celle d'aujord'hui.")
	@NotNull(message="entrez la date de naissance ce médécin.")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateNaissance;
	
	@Transient
    private MultipartFile photo;
	
	@OneToMany(mappedBy="medecin",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<MedecinSpecialite> medecinSpecialites;
	
	@OneToMany(mappedBy="medecin",cascade = CascadeType.ALL)
	private List<Creneau> creneaux;
	
	

	public Medecin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Medecin(String matricule, String nom, String prenom, Date dateNaissance, MultipartFile photo,
			List<MedecinSpecialite> medecinSpecialites) {
		super();
		this.matricule = matricule;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		this.photo = photo;
		this.medecinSpecialites = medecinSpecialites;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	
	

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}

	public List<MedecinSpecialite> getMedecinSpecialites() {
		return medecinSpecialites;
	}

	public void setMedecinSpecialites(List<MedecinSpecialite> medecinSpecialites) {
		this.medecinSpecialites = medecinSpecialites;
	}
	
	

}
