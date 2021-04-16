<%@ page pageEncoding="UTF-8" contentType="text/html" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>登录</title>
    </head>
    <body>
        <form action="login" method="post">
            名称：<input type="text" name="uname"/><br>
            密码：<input type="password" name="pword" autocomplete="off"/><br>
            <input type="checkbox" name="remember-me" id="remember_me" />
            <label for="remember_me" class="inline">Remember me</label>
             <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="submit" value="发送"/>
        </form>
    </body>
</html>