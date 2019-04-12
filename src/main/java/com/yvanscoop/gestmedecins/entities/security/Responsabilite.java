package com.yvanscoop.gestmedecins.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Responsabilite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long responsabiliteId;

    private String name;

    @OneToMany(mappedBy = "responsabilite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ClientResponsabilite> clientResponsabilites = new HashSet<>();

    public Responsabilite() {
        // TODO Auto-generated constructor stub
    }

    public Responsabilite(Long responsabiliteId, String name, Set<ClientResponsabilite> clientResponsabilites) {
        super();
        this.responsabiliteId = responsabiliteId;
        this.name = name;
        this.clientResponsabilites = clientResponsabilites;
    }

    public Long getResponsabiliteId() {
        return responsabiliteId;
    }

    public void setResponsabiliteId(Long responsabiliteId) {
        this.responsabiliteId = responsabiliteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ClientResponsabilite> getClientResponsabilites() {
        return clientResponsabilites;
    }


}
