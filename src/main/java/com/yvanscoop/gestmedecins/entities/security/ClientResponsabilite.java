package com.yvanscoop.gestmedecins.entities.security;

import javax.persistence.*;

@Entity
@Table(name = "client_responsabilite")
public class ClientResponsabilite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsabilite_id")
    private Responsabilite responsabilite;


    public ClientResponsabilite() {
        // TODO Auto-generated constructor stub
    }

    public ClientResponsabilite(Client client, Responsabilite responsabilite) {
        this.client = client;
        this.responsabilite = responsabilite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Responsabilite getResponsabilite() {
        return responsabilite;
    }

    public void setResponsabilite(Responsabilite responsabilite) {
        this.responsabilite = responsabilite;
    }


}
