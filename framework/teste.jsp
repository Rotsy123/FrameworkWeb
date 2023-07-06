<%@ page import="etu2009.framework.model.Departement" %>
 

<%
    Departement dept = (Departement)request.getAttribute("aro");
%>

<%= dept.getUpload().getNomFichier() %> 
