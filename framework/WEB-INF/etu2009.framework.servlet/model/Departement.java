package etu2009.framework.model;
import etu2009.framework.servlet.Scopeannotation;
import etu2009.framework.servlet.RestapiAnnotation;
import etu2009.framework.servlet.AuthAnnotation;

import etu2009.framework.servlet.*;

@Scopeannotation(indication="singleton")
public class Departement {
    private String nom_departement;
    private Integer nbr_departement;
    private etu2009.framework.servlet.FileUpload upload;

    public FileUpload getUpload() {
        return upload;
    }

    public void setUpload(FileUpload upload) {
        this.upload = upload;
    }
    public Departement GetDepartement(){
        Departement emp = new Departement("Departement Marketing",12);
        return emp;
    }

    @RestapiAnnotation(indication = "restapi")
    @GetUrl(url="findAllDept")
    @AuthAnnotation(admin="admin")
    public ModelView findAll(Integer id){
        Departement emp = new Departement("Departement Marketing",12);
        ModelView view  = new ModelView(this.getClass().getSimpleName());
        view.addItem("dept", emp);
        return view;
    }

    @GetUrl(url="Login")
    public ModelView Login(){
        ModelView view = new ModelView("index");
        view.addSession("isconnected", "true");
        view.addSession("profil", "true");
        return view;
    }
    @GetUrl(url="saveDept")
    public void save(){
        Departement emp = new Departement(this.getNom_departement(),this.getNbr_departement());
    }
    public Departement() {
    }
    public Departement(String nom_departement, Integer nbr_departement) {
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
    
    public void setNbr_departement(Integer nbr_departement) {
        this.nbr_departement = nbr_departement;
    }
}

    