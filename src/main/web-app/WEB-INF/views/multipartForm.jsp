<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session = "false" %>
<html>
    <head>
        <title>Spittr</title>
        <link ref="stylesheet" type="text/css"
                href="<c:url value="/resources/style.css"/>">
    </head>
    <body>
        <h1>Register</h1>
        <form method="POST" enctype="multipart/form-data">
            <input type="file" name="profilePicture" accept="image/jpeg,image/png,image/gif"/><br/>
            <input type="submit" value="Register"/>
        </form>
    </body>
</html>