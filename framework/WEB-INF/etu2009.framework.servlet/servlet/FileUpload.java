package etu2009.framework.servlet;
public class FileUpload {
    String nomFichier;
    String emplacement;
    byte [] data;
    public FileUpload(){}
    public FileUpload(String nomFichier, String emplacement, byte[] data){
        this.setNomFichier(nomFichier);
        this.setEmplacement(emplacement);
        this.setData(data);
    }
    
    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
}