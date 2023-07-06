package etu2009.framework.model;
import etu2009.framework.servlet.*;

public class Departement {
    String nom_departement;
    Double nbr_departement;
    private etu2009.framework.servlet.FileUpload upload;

    public FileUpload getUpload() {
        return upload;
    }

    public void setUpload(FileUpload upload) { 
        this.upload = upload;
    }
    @GetUrl(url = "findAllDept")
    public Departement findAll() {
        Departement emp = new Departement(getNom_departement(), getNbr_departement(), getUpload());
        return emp;
    }

    @GetUrl(url = "saveDept")
    public void save() {
        Departement emp = new Departement(this.getNom_departement(), this.getNbr_departement(),this.getUpload());
    }

    public Departement() {
    }
    public Departement(String nom_departement, Double nbr_departement, FileUpload upload) {
        this.nom_departement = nom_departement;
        this.nbr_departement = nbr_departement;
        this.upload = upload;
    }
    public Departement(String nom_departement, Double nbr_departement) {
        this.nom_departement = nom_departement;
        this.nbr_departement = nbr_departement;
    }

    public String getNom_departement() {
        return nom_departement;
    }

    public void setNom_departement(String nom_departement) {
        this.nom_departement = nom_departement;
    }

    public Double getNbr_departement() {
        return nbr_departement;
    }

    public void setNbr_departement(String nbr_departement) {
        double dept = Double.parseDouble(nbr_departement);
        this.nbr_departement = dept;
    }

    public void getAtt(Double nbr_departement) {
        this.nbr_departement = nbr_departement;
    }
}
