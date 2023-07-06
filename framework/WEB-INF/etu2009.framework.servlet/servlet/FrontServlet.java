package etu2009.framework.servlet;
import etu2009.framework.model.Employe;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.http.Part;

import jakarta.servlet.RequestDispatcher;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import javax.security.auth.x500.X500Principal;
import javax.swing.text.View;

import org.apache.commons.io.FilenameUtils; 
import jakarta.servlet.annotation.MultipartConfig;

 
@MultipartConfig
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
    private  String getFileName(jakarta.servlet.http.Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] parts = contentDisposition.split(";");
        for (String partStr : parts) {
            if (partStr.trim().startsWith("filename"))
                return partStr.substring(partStr.indexOf('=') + 1).trim().replace("\"", "");
        }
        return null;
    }
    private FileUpload fillFileUpload( FileUpload file, jakarta.servlet.http.Part filepart ){
        try (InputStream io = filepart.getInputStream()) {
            ByteArrayOutputStream buffers = new ByteArrayOutputStream();
            byte[] buffer = new byte[(int) filepart.getSize()];
            int read;
            while ((read = io.read(buffer, 0, buffer.length)) != -1) {
                buffers.write(buffer, 0, read);
            }
            file.setNomFichier(this.getFileName(filepart));
            file.setData(buffers.toByteArray());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
     public  FileUpload fileTraitement( Collection<jakarta.servlet.http.Part> files, Field field ){
        FileUpload file = new FileUpload();
        String name = field.getName();
        boolean exists = false;
        String filename = null;
        jakarta.servlet.http.Part filepart = null;
        for (jakarta.servlet.http.Part part : files) {
            if (part.getName().equals(name)) {
                filepart = part;
                exists = true;
                break;
            }
        }
        file = this.fillFileUpload(file, filepart);
        return file;
    }
    private void handleFile( Class<?> classs, HttpServletRequest request, Object object, HttpServletResponse response )throws Exception{ 
        PrintWriter out = response.getWriter();
        out.println("1522222222222X500Principal");
        Field[] fields = classs.getDeclaredFields();
        try {
            Collection<Part> files = request.getParts();
            for (Field f : fields) {
                String capitalized = Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1);
                if (f.getType() == etu2009.framework.servlet.FileUpload.class) {   
                    out.println(capitalized+"  "+f.getType());                
                    Method m = classs.getMethod("set"+capitalized, f.getType());
                    Object o = this.fileTraitement(files, f); 
                    m.invoke(object, o); 
                }                

            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e);
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
                    out.println(name);
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
                      action(m.getMethod(),key,null,o,request,response); 
                        int paramCount = mets.getParameterCount();
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
                    if(fields[i].getType()!=FileUpload.class){
                        String capitalized = Character.toUpperCase(fields[i].getName().charAt(0)) + fields[i].getName().substring(1);
                        out.println(capitalized);
                        Method temp = clazz.getDeclaredMethod("get"+ capitalized);
                        Object value = request.getParameter(fields[i].getName());
                        listM[i] = clazz.getDeclaredMethod("set"+ capitalized,String.class);
                        listM[i].invoke(o, value);
                        out.println(temp.invoke(o, null).toString());
                    }else{
                        out.println("ok"); 
                    } 
                }
                this.handleFile(clazz, request, o, response);
                ModelView view = new ModelView("teste.jsp");
                view.addItem("aro", o);
                request.setAttribute("aro",o); 
                
                RequestDispatcher dispat = request.getRequestDispatcher(view.getUrl());
                dispat.forward(request, response);                    
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