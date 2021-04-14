package pers.mortal.learn.springmvc.multipart;

import org.springframework.context.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;

@Configuration
public class ExampleMultipartConfig {
    public static int select = 1;
    public static String path = "D:/tmp/part";
            ;
    @Bean
    @Conditional(StandardServletMultipartResolverCondition.class)
    public StandardServletMultipartResolver standardServletMultipartResolver(){
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        path = "D:/tmp/standard";
        return multipartResolver;
    }

    @Bean
    @Conditional(CommonsMultipartResolverCondition.class)
    public CommonsMultipartResolver commonsMultipartResolver() throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setUploadTempDir(new FileSystemResource("D:/tmp/commons"));
        multipartResolver.setMaxUploadSize(1024 * 1024 * 10);
        multipartResolver.setMaxInMemorySize(1024 * 1024 );
        path = "D:/tmp/commons";
        return multipartResolver;
    }
}

class StandardServletMultipartResolverCondition implements Condition{

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        if(ExampleMultipartConfig.select == 1){
            return true;
        }
        return false;
    }
}
class CommonsMultipartResolverCondition implements Condition{

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        if(ExampleMultipartConfig.select == 2){
            return true;
        }
        return false;
    }
}
