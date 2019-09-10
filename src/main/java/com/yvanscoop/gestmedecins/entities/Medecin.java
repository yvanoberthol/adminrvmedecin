package com.yvanscoop.gestmedecins.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Medecin {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "entrez un matricule pour ce médécin.")
    @Length(min = 4, message = "le matricule doit compter au moins 4 caractères.")
    @Column(nullable = false, unique = true)
    private String matricule;

    @NotEmpty(message = "entrez un nom pour ce médécin.")
    @Length(min = 4, message = "le nom du médécin doit compter au moins 4 caractères.")
    @Column(nullable = false)
    private String nom;

    @NotEmpty(message = "entrez un prénom pour ce médécin.")
    @Length(min = 4, message = "le prénom du médécin doit compter au moins 4 caractères.")
    @Column(nullable = false)
    private String prenom;

    @NotEmpty(message = "entrez un Email pour ce médécin.")
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "entrez le numéro de téléphone du médécin.")
    @Length(min = 9,message = "Le numéro de téléphone est de 9 chiffres")
    @Column(nullable = false)
    private String telephone;

    @Past(message = "la date de naissance doit être antérieure à celle d'aujord'hui.")
    @NotNull(message = "entrez la date de naissance ce médécin.")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;

    @NotEmpty(message = "entrez la ville de résidence du médécin.")
    @Column(nullable = true)
    private String ville;

    @Transient
    private MultipartFile photo;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MedecinSpecialite> medecinSpecialites;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    private List<Creneau> creneaux;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "medecin")
    private CompteMedecin compteMedecin;


    public Medecin() {
        super();
        // TODO Auto-generated constructor stub
    }
}
