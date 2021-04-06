package pers.mortal.learn.springmvc;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


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
        return new Class<?>[]{WebConfig.class};
    }


}
