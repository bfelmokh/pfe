package com.pfe.elmokhtar.domotique.RV;

/**
 * Created by benfraj on 25/04/2015.
 */
public class item {
    private int id;
    private String nom;

    public item(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }


}