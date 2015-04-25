package com.pfe.elmokhtar.domotique.RV;

/**
 * Created by benfraj on 25/04/2015.
 */
public class item {
    private String id;
    private String nom;

    public item(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }


}