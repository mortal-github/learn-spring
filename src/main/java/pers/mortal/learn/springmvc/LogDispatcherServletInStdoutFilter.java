package pers.mortal.learn.springmvc;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogDispatcherServletInStdoutFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        System.out.println("begin do filter -- LogInStdout<br>");
        chain.doFilter(request, response);
        System.out.println("ended do filter -- LogInStdout<br>");
    }
}
