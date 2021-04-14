package pers.mortal.learn.springmvc;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class OtherServletInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext context){
        //添加其他Servlet
        ServletRegistration.Dynamic myServlet = context.addServlet("myServlet", OtherServlet.class);
        myServlet.addMapping("/custom/*");
        //添加Filter和Listener
        FilterRegistration.Dynamic filter = context.addFilter("myFilter", OtherFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/custom/*");
    }
}
