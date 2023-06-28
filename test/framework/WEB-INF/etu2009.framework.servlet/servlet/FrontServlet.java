package etu2009.framework.servlet;
import etu2009.framework.model.Employe;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import javax.swing.text.View;

import org.apache.commons.io.FilenameUtils;

public class FrontServlet extends HttpServlet {
      HashMap<String,Mapping> MappingUrls;
    public void init (){
        // PrintWriter out = response.getWriter();
        MappingUrls = new HashMap<>();
          try {
            String directory =getServletContext().getRealPath("/WEB-INF/etu2009.framework.servlet/model");
            String [] classe = reset(directory);
            for(int i =0 ;i< classe.length; i++){
                 String className = classe[i];
                className = "etu2009.framework.model." +className;
                Class<?> clazz;
                clazz = Class.forName(className);
                Method [] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                     Annotation[] an = method.getAnnotations();
                     if(an.length!=0){
                         GetUrl annotation = method.getAnnotation(GetUrl.class);
                         MappingUrls.put(annotation.url(),new Mapping(className,method.getName()));
                     }
                }


            }
         } catch (Exception ex) {
              ex.printStackTrace();
         }
    }
    public String[] reset(String Directory){
        ArrayList<String> rar=new ArrayList<>();
        File dossier = new File(Directory);
        String[] contenu = dossier.list();
        for(int i=0; i<contenu.length; i++){
            String fe=FilenameUtils.getExtension(contenu[i]);
            if(fe.equalsIgnoreCase("java")){
                String [] value = contenu[i].split("[.]");
                rar.add(value[0]);
            }
        }
       return rar.toArray(new String[rar.size()]); 
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
         PrintWriter out = response.getWriter();
                try{                   
                    String url = request.getRequestURI();
                    String[]split = url.split("/", 0);
                    Mapping m = MappingUrls.get(request.getRequestURI().replace(request.getContextPath()+"/",""));
                    String key = request.getRequestURI().replace(request.getContextPath()+"/","");
                    String name =m.getClassName();
                     
                    Object o =Class.forName(name).getConstructor().newInstance(null);
                    Method[] methods = o.getClass().getMethods();
                    Method mets = null;
                    for(int i =0; i<methods.length; i++){
                        if(methods[i].getName().equalsIgnoreCase(m.getMethod())){
                            mets = methods[i];
                            break;
                        }
                    }
                    Parameter [] parametre = mets.getParameters();
                    Object [] objet = new Object[parametre.length];
                    for(int i = 0; i<parametre.length; i++){
                        Object value = request.getParameter(parametre[i].getName());
                        objet[i] = value;
                    }
                    Object vao = mets.invoke(o, objet);
                    int paramCount = mets.getParameterCount();
                    action(m.getMethod(),key,vao,o,request,response);
                }catch(Exception e){
                    out.println(e);
                }
        }
        public void action(String methodes,String key,Object vao,Object o,HttpServletRequest request, HttpServletResponse response) throws Exception{
            PrintWriter out = response.getWriter();
            if (methodes.compareToIgnoreCase("findAll")==0){                  
                ModelView view = new ModelView(vao.getClass().getSimpleName());
                view.addItem(key, vao);
                request.setAttribute(key,view.getData());
                String viewUrl = view.getUrl()+".jsp";
                RequestDispatcher dispat = request.getRequestDispatcher(viewUrl);
                dispat.forward(request, response);
            }
            else if(methodes.compareToIgnoreCase("save") == 0){                    
                Class<?> clazz = o.getClass();
                Field[] fields = clazz.getDeclaredFields();
                Method[] listM = new Method[fields.length];
                for(int i =0; i<fields.length; i++){
                    String capitalized = Character.toUpperCase(fields[i].getName().charAt(0)) + fields[i].getName().substring(1);
                    out.println(capitalized+ "cap");
                    Method temp = clazz.getDeclaredMethod("get"+ capitalized);
                    Object value = request.getParameter(fields[i].getName());
                    listM[i] = clazz.getDeclaredMethod("set"+ capitalized,String.class);
                    listM[i].invoke(o, value);
                    out.println(temp.invoke(o, null).toString()+"temp   ");
            }
            }
        }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          
          try {
              processRequest(request, response);
          } catch (Exception ex) {
              Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
          }
         
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
          try {
              processRequest(request, response);
          } catch (Exception ex) {
              Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
          }
         
    }
}