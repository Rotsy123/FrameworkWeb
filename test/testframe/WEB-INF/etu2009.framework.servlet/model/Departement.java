package etu2009.framework.model;

import etu2009.framework.servlet.*;


public class Departement {
    private String nom_departement;
    private int nbr_departement;
    
    @GetUrl(url="findAllDept")
    public Departement findAll(){
        Departement emp = new Departement(getNom_departement(),getNbr_departement());
        return emp;
    }
    public Departement() {
    }
    public Departement(String nom_departement, int nbr_departement) {
        this.nom_departement = nom_departement;
        this.nbr_departement = nbr_departement;
    }


    
    public String getNom_departement() {
        return nom_departement;
    }

    public void setNom_departement(String nom_departement) {
        this.nom_departement = nom_departement;
    }

    public int getNbr_departement() {
        return nbr_departement;
    }

    public void setNbr_departement(int nbr_departement) {
        this.nbr_departement = nbr_departement;
    }
}
