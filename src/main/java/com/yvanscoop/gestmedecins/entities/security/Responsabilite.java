package com.yvanscoop.gestmedecins.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Responsabilite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "responsabilite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ClientResponsabilite> clientResponsabilites = new HashSet<>();

    public Responsabilite() {
        // TODO Auto-generated constructor stub
    }

    public Responsabilite(Long id, String name, Set<ClientResponsabilite> clientResponsabilites) {
        super();
        this.id = id;
        this.name = name;
        this.clientResponsabilites = clientResponsabilites;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
