package com.yvanscoop.gestmedecins.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

@Entity
@Table(name = "creneaux")
@Getter @Setter
public class Creneau {

    @Id
    @GeneratedValue
    private Long id;

    @Range(min = 7, max = 18, message = "7 à 18")
    private int hdebut;

    @Range(min = 0, max = 59, message = "0 à 59")
    private int mdebut;

    @Range(min = 7, max = 18, message = "7 à 18")
    private int hfin;

    @Range(min = 0, max = 59, message = "0 à 59")
    private int mfin;

    //creneau lié au medecin
    @ManyToOne
    @JoinColumn(name = "id_medecin")
    @JsonIgnore
    private Medecin medecin;

    public Creneau() {
        // TODO Auto-generated constructor stub
    }


    public Creneau(int hdebut, int mdebut, int hfin, int mfin, Medecin medecin) {
        this.medecin = medecin;
        this.hdebut = hdebut;
        this.mdebut = mdebut;
        this.hfin = hfin;
        this.mfin = mfin;
    }

    public String getCreneauAll(){
        return hdebut+":"+mdebut+" -- "+hfin+":"+mfin;
    }

}
