<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*"%>
<%@ page session="false" contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Exception</title>
    </head>
    <body>
        <h1>error</h1>
        <%
            out.println("列举属性<br>");

            Collection<String> list = Collections.list(request.getParameterNames());
            for(String attribute : list){
                out.println(attribute + " : " + request.getParameter(attribute) + "<br>");
            }

            out.println("结束列举<br>");
        %>
    </body>
</html>
