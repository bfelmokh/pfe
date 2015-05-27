package com.pfe.elmokhtar.domotique.Scenario;

/**
 * Created by benfraj on 24/05/2015.
 */

/**
 * Created by benfraj on 25/04/2015.
 */
public class item {

    private String id;
    private String libelle;
    private String peri;
    public item(String id, String peri,String libelle) {
        this.id=id;
        this.libelle = libelle;
        this.peri = peri;
    }



    public String getId() {
        return id;
    }
    public String getLibelle() {
        return libelle;
    }
    public String getPeri() {
        return peri;
    }


}