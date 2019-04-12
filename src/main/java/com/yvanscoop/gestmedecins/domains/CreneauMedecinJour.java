package com.yvanscoop.gestmedecins.domains;


import com.yvanscoop.gestmedecins.entities.Creneau;
import com.yvanscoop.gestmedecins.entities.Rv;

public class CreneauMedecinJour {

    // champs
    private Creneau creneau;
    private Rv rv;

    // constructeurs
    public CreneauMedecinJour() {

    }

    public CreneauMedecinJour(Creneau creneau, Rv rv) {
        this.creneau = creneau;
        this.rv = rv;
    }

    // getters et setters

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public Rv getRv() {
        return rv;
    }

    public void setRv(Rv rv) {
        this.rv = rv;
    }

}
