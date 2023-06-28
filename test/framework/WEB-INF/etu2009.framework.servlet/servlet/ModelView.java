package etu2009.framework.servlet;
import java.util.HashMap;


public class ModelView {
    String url;
    HashMap<String,Object> data;

    public void addItem(String key, Object valeur){
        if(this.data==null){
            this.data= new HashMap<>();
        }
        this.data.put(key, valeur);
    }

    public HashMap getData(){
        return data;
    }
    public void setdata(HashMap<String,Object>data ){
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ModelView(String url){
        this.url = url;
    }

}
