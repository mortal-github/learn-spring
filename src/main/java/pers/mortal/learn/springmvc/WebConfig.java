package pers.mortal.learn.springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import pers.mortal.learn.springdata.security.ExampleMethodSecurityController;
import pers.mortal.learn.springintegration.restapi.RESTController;
import pers.mortal.learn.springmvc.multipart.ExampleMultipartConfig;

import java.util.HashMap;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {WebConfig.class, ExampleMethodSecurityController.class, RESTController.class})
@Import({ExampleMultipartConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver irViewResolver(){
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();

        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);

        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }

    //配置ContentNegotiationManager
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
        //默认内容类型。
        configurer.defaultContentType(MediaType.TEXT_HTML);
        //根据扩展名指定内容类型
        configurer.favorPathExtension(true);
        //根据请求参数指定内容类型。
        configurer.favorParameter(true);
        configurer.parameterName("format");//设置确当内容类型的请求参数名，默认为format
        //忽视请求的Accept头信息。
        //configurer.ignoreAcceptHeader(true);
        //将请求的扩展名映射为特定的媒体类型
        configurer.mediaTypes(new HashMap<String, MediaType>(){{
            put("my_type", MediaType.APPLICATION_JSON);
        }});
    }
    //配置ContentNegotiatingViewResolver
    @Bean
    public ViewResolver cnViewResolver(ContentNegotiationManager contentNegotiationManager, List<ViewResolver> viewResolvers){
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(contentNegotiationManager);
        resolver.setViewResolvers(viewResolvers);
        return resolver;
    }
    //配置其他ViewResolver.
    @Bean ViewResolver beanNameViewResolver(){
        return new BeanNameViewResolver();
    }
    //Bean视图
    @Bean
    public View beanView(){
        return new MappingJackson2JsonView();
    }
}
