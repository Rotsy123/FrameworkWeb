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
import java.util.Set;
import org.json.simple.JSONObject;
import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;


import javax.security.auth.x500.X500Principal;
import javax.swing.text.View;

import org.apache.commons.io.FilenameUtils; 
import jakarta.servlet.annotation.MultipartConfig;

 
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
                        String value = scope.indication(); 
                        k++;
                        Object ob = clazz;
                        singleton.put(clazz.getName(), ob);
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
    public void redirect(ModelView nomjs ,HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException ,Exception{ 
        PrintWriter out=res.getWriter();
        HashMap<String,Object> dataMap = nomjs.getData();        
        Method isjon = nomjs.getClass().getMethod("getIsjson",new Class[0]);
       
        if((Boolean) isjon.invoke(nomjs, (Object[])null)==true){
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                jsonObject.put(key, value);
            }
            String json = jsonObject.toJSONString();
            out.println(json);
            out.println("tafifitra");
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
        out.println(k+" longueur");
        out.println(singleton.size()+ "size");
        Set<String> cles = singleton.keySet();
        out.println(cles.size());        
        try{                   
            String url = request.getRequestURI();
            String[]split = url.split("/", 0);
            Mapping m = MappingUrls.get(request.getRequestURI().replace(request.getContextPath()+"/",""));
            String key = request.getRequestURI().replace(request.getContextPath()+"/","");
            String name =m.getClassName();
            out.println(name);
            Class<?> clazz;
            clazz = Class.forName(name);
            Class test = Class.forName(name);
            Object o=null;
            if(singleton.get(test)!=null){
                o = singleton.get(test);
            }else{
                o = Class.forName(name).getConstructor().newInstance(null);

            }
            out.println(o.getClass()+"tyyyyyyyyyy");
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
            Object vao = o.getClass().getMethod(m.getMethod()).invoke(o);
            action(m.getMethod(), key, vao, o, request, response); 
            int paramCount = mets.getParameterCount();
        }catch(Exception e){
            throw(e);
        }
        }
        public void action(String methodes,String key,Object vao,Object o,HttpServletRequest request, HttpServletResponse response) throws Exception{
            PrintWriter out = response.getWriter();
            if (methodes.compareToIgnoreCase("findAll")==0){                  
                ModelView view = new ModelView(vao.getClass().getSimpleName());
                view.addItem(key, vao);
                request.setAttribute(key,view.getData());
                String viewUrl = view.getUrl()+".jsp";
                // if(view.getUrl().equalsIgnoreCase("Employe")){
                //     view.setIsjson(true);
                // }
                redirect(view, request, response);
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