package etu2009.framework.model;
import etu2009.framework.servlet.GetUrl;

public class Employe {
    
    String name;
    int ages ;
    
    @GetUrl(url="findAllEmp")
    public Employe findAll(){
        Employe emp = new Employe(getName(),getAges());
        return emp;
    }

    public Employe() {
    }
    
    public Employe(String name, int ages) {
        this.name = name;
        this.ages = ages;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAges() {
        return ages;
    }

    public void setAges(int ages) {
        this.ages = ages;
    }
    
    
}
