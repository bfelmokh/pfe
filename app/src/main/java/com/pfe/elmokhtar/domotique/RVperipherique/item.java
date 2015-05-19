package com.pfe.elmokhtar.domotique.RVperipherique;

/**
 * Created by benfraj on 30/04/2015.
 */
public class item {
    private String id;
    private String nom;
    private String value;

    public item(String id, String nom,String value) {
        this.id = id;
        this.nom = nom;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
    public Boolean getValue(){

        if(value.equals("1"))
            return true;
        else
            return false;
    }
}
