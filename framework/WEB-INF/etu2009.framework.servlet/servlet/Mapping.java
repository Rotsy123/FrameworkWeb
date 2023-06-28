package etu2009.framework.servlet;

public class Mapping{
    String className;
    String method;

    public String getClassName(){
        return this.className;
    }
    public void setClassName(String className){
        this.className = className;
    }
    
    public String getMethod(){
        return this.method;
    }
    public void setMethod(String method){
        this.method = method;
    }
    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }
}