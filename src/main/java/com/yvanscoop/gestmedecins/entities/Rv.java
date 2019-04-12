package com.yvanscoop.gestmedecins.entities;

import com.yvanscoop.gestmedecins.entities.security.Client;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rv")
public class Rv {

    @Id
    @GeneratedValue
    private Long id;
    // caractéristiques d'un Rv
    @Temporal(TemporalType.DATE)
    private Date jour;

    // un rv est lié à un client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
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


    // getters et setters

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getJour() {
        return jour;
    }

    public void setJour(Date jour) {
        this.jour = jour;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

}