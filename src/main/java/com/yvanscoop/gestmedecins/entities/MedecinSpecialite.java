package com.yvanscoop.gestmedecins.entities;

import javax.persistence.*;

@Entity
@Table(name = "medecin_specialite")
public class MedecinSpecialite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    @ManyToOne
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;

    public MedecinSpecialite() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MedecinSpecialite(Medecin medecin, Specialite specialite) {
        this.medecin = medecin;
        this.specialite = specialite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }


}
