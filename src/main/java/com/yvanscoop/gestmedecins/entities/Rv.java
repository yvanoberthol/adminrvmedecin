package com.yvanscoop.gestmedecins.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yvanscoop.gestmedecins.entities.security.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rv")
@Getter @Setter
public class Rv implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    // caractéristiques d'un Rv
    @Temporal(TemporalType.DATE)
    private Date jour;

    // un rv est lié à un client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    @JsonIgnore
    private Client client;

    // un rv est lié à un créneau
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creneau")
    private Creneau creneau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_specialite")
    private Specialite specialite;


    private Boolean annule = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rv")
    @JsonIgnore
    private TokenRv tokenRv;

    // constructeur par défaut
    public Rv() {
    }

    // avec paramètres
    public Rv(Date jour, Client client, Creneau creneau) {
        this.jour = jour;
        this.client = client;
        this.creneau = creneau;
    }
}