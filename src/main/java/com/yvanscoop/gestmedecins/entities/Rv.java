package com.yvanscoop.gestmedecins.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.yvanscoop.gestmedecins.entities.security.Client;

@Entity
@Table(name = "rv")
public class Rv{

	@Id @GeneratedValue
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

	public Date getJour() {
		return jour;
	}

	public void setJour(Date jour) {
		this.jour = jour;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Creneau getCreneau() {
		return creneau;
	}

	public void setCreneau(Creneau creneau) {
		this.creneau = creneau;
	}

}