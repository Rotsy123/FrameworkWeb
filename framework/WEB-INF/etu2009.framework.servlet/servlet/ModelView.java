package etu2009.framework.servlet;
import java.util.HashMap;


public class ModelView {
    String url;
    HashMap<String,Object> data;
    HashMap<String,Object> session=new HashMap<String,Object>();
    Boolean isjson = true;
    
    public void setIsjson(Boolean t){
        isjson=t;
     }
     public Boolean getIsjson(){
        return isjson;
     }
    public void addSession(String key,Object valeur){
        session.put(key, valeur);
    }
    public HashMap<String,Object> getSession(){
        return session;
    }
    public void addItem(String key, Object valeur){
        if(this.data==null){
            this.data= new HashMap<>();
        }
        this.data.put(key, valeur);
    }

    public HashMap<String,Object> getData(){
        return data;
    }
    public void setdata(HashMap<String,Object>data ){
        this.data = data;
    }

    public String getUrl() { 
        return url;
    }

    public void setUrl(String url) {
        this.url = "/"+url+".jsp";
    }

    public ModelView(String url){
        setUrl(url);
    }

}
