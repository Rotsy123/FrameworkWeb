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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONObject;
import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;


import javax.security.auth.x500.X500Principal;
import javax.swing.text.View;

import org.apache.commons.io.FilenameUtils; 
import jakarta.servlet.annotation.MultipartConfig;
import com.google.gson.Gson;

 
@MultipartConfig
public class FrontServlet extends HttpServlet {
    HashMap<String,Mapping> MappingUrls;
    HashMap<String,Object> singleton;
    HashMap<String ,Object> session= new HashMap<String ,Object>();
    int k=0; 
    public void setsession(String nom,Object o){
        session.put(nom, o);
    }
    public HashMap<String ,Object> getsession(){
        return session;
    }
    public void init (){ 
        MappingUrls = new HashMap<>();
        singleton = new HashMap<>();
        try {
            String directory = getServletContext().getRealPath("/WEB-INF/etu2009.framework.servlet/model");
            String [] classe = reset(directory);
            for(int i =0 ;i< classe.length; i++){
                String className = classe[i];
                className = "etu2009.framework.model." +className;
                Class<?> clazz;
                clazz = Class.forName(className);
                Scopeannotation scope = clazz.getAnnotation(Scopeannotation.class);
                if(scope!=null){
                    // String value = scope.indication(); 
                    k++;
                    Object ob = clazz;
                    singleton.put(clazz.getName(), null);
                } 
                Method [] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    Annotation[] an = method.getAnnotations();
                    if(an.length!=0){
                        GetUrl annotation = method.getAnnotation(GetUrl.class);
                        MappingUrls.put(annotation.url(),new Mapping(className,method.getName()));
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void ObjectToJson(Object o,PrintWriter out ){
        Gson gson = new Gson();
        String json = gson.toJson(o); 
    }
    public void redirect(ModelView nomjs ,HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException ,Exception{ 
        PrintWriter out=res.getWriter();
        HashMap<String,Object> dataMap = nomjs.getData();        
        Method isjon = nomjs.getClass().getMethod("getIsjson",new Class[0]);
        if((Boolean) isjon.invoke(nomjs, (Object[])null) == true){
            Gson gson = new Gson();
            String json = gson.toJson(nomjs.getData()); 
            out.println("tafiditraaaa");
            RequestDispatcher dispat = req.getRequestDispatcher(nomjs.getUrl());
            dispat.forward(req, res);
            return ;             
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
        Field[] fields = classs.getDeclaredFields();
        try {
            Collection<Part> files = request.getParts();
            for (Field f : fields) {
                String capitalized = Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1);
                if (f.getType() == etu2009.framework.servlet.FileUpload.class) {    
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
    
    public Object[] ObjetParametre(Method mets,HttpServletRequest request){
        Parameter [] parametre = mets.getParameters();
        Class<?>[] tableClass = mets.getParameterTypes();
        Object [] objet = new Object[tableClass.length];
        for(int i = 0; i<parametre.length; i++){
            String value = request.getParameter(parametre[i].getName());
            objet[i] = cast(value,tableClass);
        }
        return objet;
    }
    public void Dispatch(ModelView view,HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(view != null){
            if(view.getData().size()!=0){
                    for(Map.Entry<String,Object> entry: view.getData().entrySet()){
                        String key1 = entry.getKey();
                        Object value = entry.getValue();
                        request.setAttribute(key1,value);
                    }
                }
            RequestDispatcher dispat = request.getRequestDispatcher(view.getUrl());
            dispat.forward(request, response);
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
            PrintWriter out = response.getWriter();
            out.println(MappingUrls.size());
            try{
                Mapping m = MappingUrls.get(request.getRequestURI().replace(request.getContextPath()+"/",""));
                String key = request.getRequestURI().replace(request.getContextPath()+"/","");
                String name = m.getClassName();
                Object o = null;
                Class classe = Class.forName(name);
                if(singleton.containsKey(classe.getName())){
                    if(singleton.get(classe.getName())==null){
                        singleton.put(classe.getName(), classe.getConstructor().newInstance());
                    }
                    o = singleton.get(classe.getName());
                }else{
                    o = Class.forName(name).getConstructor().newInstance(null);
                }
                System.out.println(o);
                Method[] methods = o.getClass().getMethods();
                Method mets = null;
                for(int i =0; i<methods.length; i++){
                    if(methods[i].getName().equalsIgnoreCase(m.getMethod())){
                        mets = methods[i];
                        break;
                    }
                }
                
                int paramCount = mets.getParameterCount();
                ModelView view;
                Object [] objet;
                if(paramCount == 0){
                    objet = this.ObjetParametre(mets, request);
                    Class<?> clazz = o.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    Method[] listM = new Method[fields.length];
                    for(int i = 0; i<fields.length;i++){   
                      if(fields[i].getType()!= FileUpload.class){
                        listM[i] = clazz.getDeclaredMethod("set"+ Capitalized(fields[i].getName()),fields[i].getType());
                        Method temp = clazz.getDeclaredMethod("get"+ Capitalized(fields[i].getName()));
                        String value = request.getParameter(fields[i].getName());
                        Object ob = caste(value,fields[i].getType());
                        listM[i].invoke(o,ob );
                        out.println(temp.invoke(o, null).toString());
                      }
                      this.handleFile(clazz, request, o, response);
                }
                ModelView views = new ModelView("teste");
                views.addItem("aro", o);
                request.setAttribute("aro",o);
                RequestDispatcher dispat = request.getRequestDispatcher(views.getUrl());
                dispat.forward(request, response);
                view = (ModelView)mets.invoke(o, null);
                
                }else{
                    objet = this.ObjetParametre(mets, request);                    
                    view = (ModelView)mets.invoke(o,objet);
                }
                view.setIsjson(true);
                if(view.getIsjson()){
                    String json = new Gson().toJson(view.getData());
                    out.println(json);
                }
            }catch(Exception e){
                e.printStackTrace(out);
            }
        
    }
    public Object caste(String acaster,Class classe){
         Object vao = acaster;
         if(classe == Double.class){
            vao = Double.parseDouble(acaster);
        }
        else if(classe == Integer.class){
            vao = Integer.parseInt(acaster);
        }
        else if(classe == Date.class){
            vao = Date.valueOf(acaster);
        }
         return vao;
    }
    public Object cast(String acaster,Class<?> [] classTable){
        Object vao = acaster;
        for(int i =0;i<classTable.length;i++){
            if(classTable[i]==Double.class){
                vao = Double.parseDouble(acaster);
            }
            else if(classTable[i]==Integer.class){
                vao = Integer.parseInt(acaster);
            }
            else if(classTable[i]== Date.class){
                vao = Date.valueOf(acaster);
            }
        }
        return vao;
    }
    public String Capitalized(String lettre){
        String capitalized = Character.toUpperCase(lettre.charAt(0)) + lettre.substring(1);
        return capitalized;
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