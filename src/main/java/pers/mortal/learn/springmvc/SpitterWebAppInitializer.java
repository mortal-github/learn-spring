package pers.mortal.learn.springmvc;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import pers.mortal.learn.springintegration.websocket.ExampleAbstractWebSocketMessageBrokerConfigurer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

public class SpitterWebAppInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};       //将DispatcherServlet映射到"/"
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {//返回的带 有@Configuration注解的类将会用来配置ContextLoaderListener创建的应用上下文中的bean
        return new Class<?>[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {//带有@Configuration注解的类将会用来定义DispatcherServlet应用上下文中的bean。
        return new Class<?>[]{WebConfig.class, ExampleAbstractWebSocketMessageBrokerConfigurer.class};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration){
        //设置Multipart配置
        registration.setMultipartConfig(
                new MultipartConfigElement("D:/tmp", 1024 * 1024, 1024 * 1024 * 10, 1024));
        //设置优先级
        registration.setLoadOnStartup(-1);
        //设置初始化参数
        registration.setInitParameter("param", "value");
    }

    //注册Filter并映射到DispatcherServlet。
    @Override
    protected Filter[] getServletFilters(){
        return new Filter[]{new LogDispatcherServletInStdoutFilter()};
    }
}
