<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
    <head>
        <title>Spittles</title>
    </head>
    <body>
        <h1>One Spittle</h1>
        <div class="spittleView">
            <div class="spittleMessage"><c:out value="${spittle.message}"/></div>
            <div><span class="spittleTime"><c:out value="${spittle.time}"/></span></div>
        </div>
    </body>
</html>
