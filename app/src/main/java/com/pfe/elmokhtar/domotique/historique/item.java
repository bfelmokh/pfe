package com.pfe.elmokhtar.domotique.historique;

/**
 * Created by benfraj on 25/04/2015.
 */
public class item {

    private String nom;
    private String date;
    private String pseudo;
    public item(String id, String nom, String date, String pseudo) {
        this.pseudo=pseudo;
        this.nom = nom;
        this.date = date;
    }



    public String getNom() {
        return nom;
    }
    public String getDate() {
        return date;
    }
    public String getPseudo() {
        return pseudo;
    }


}