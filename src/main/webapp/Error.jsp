<%-- 

 Copyright 2014 Hogeschool Leiden.
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
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
