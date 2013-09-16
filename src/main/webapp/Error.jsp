<%-- 
    Document   : Error
    Created on : Aug 2, 2013, 11:03:02 AM
    Author     : hl
--%>
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>


<%
    out.println("{ RequestUri : " + pageContext.getErrorData().getRequestURI() + ",");
    out.println("HttpStatusCode : " + pageContext.getErrorData().getStatusCode() + ",");
    out.println("ErrorMessage : " + pageContext.getException() + " }");

%>
