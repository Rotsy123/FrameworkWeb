package etu2009.framework.servlet;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import java.lang.String;
import java.lang.reflect.*;
import java.rmi.server.ObjID;
import java.util.HashMap;
import java.util.Vector;
import etu002087.framework.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.*;
import java.util.regex.Pattern;

import javax.print.DocFlavor.STRING;

import java.util.regex.Matcher;

public class FrontS extends HttpServlet{
    String baseurl;
    String nompakage;
    HashMap<String,Mapping> MappingUrls ;

    public void init() throws ServletException {
        //alimentation de l'attribut MappingUrls
        MappingUrls= new HashMap<String,Mapping>();
       //class respensable de prendre recurcivement le class dans le dossier
        Vector<String> classdefin = new Vector<>();
        this.getclass(nompakage,classdefin,".class");//avoir tout le classe
            for(String cheminclass :classdefin){
                try{
                    String string = (cheminclass.split(this.getInitParameter("split_class")))[1].replace('/','.');
                    if(string.contains("annotation")!=true){
                        String[] st = string.split(".class");
                        Class c = Class.forName(st[0]); 
                        //si la classe est une singleton alors ajouter dans le hashmap
                        //isAnnotationPresent no nampiasaina mba amatarana oe tena io anatation io marina no amin'le class
                        if(c.isAnnotationPresent(Scopeannotation.class)){
                            Scopeannotation identification=(Scopeannotation)c.getAnnotation(Scopeannotation.class);
                            if(identification.indication().compareTo("singleton")==0){
                                Singleton.put(c.getName(),null);
                            }
                        }
                        for(Method method :c.getDeclaredMethods()){
                            if(method.isAnnotationPresent(Urlannotation.class) ){
                                Urlannotation index = method.getAnnotation(Urlannotation.class);
                                MappingUrls.put(index.index(),new Mapping(st[0], method.getName()));
                            }
                        }
                    } 
                }
                catch(Exception e){
                }    
            }
        
    }
}
