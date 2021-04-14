package pers.mortal.learn.springmvc;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class OtherFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.println("begin do filter -- MyFilter<br>");
        chain.doFilter(request, response);
        writer.println("ended do filter -- MyFilter<br>");
    }
}
