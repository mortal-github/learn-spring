package pers.mortal.learn.spring.advancewiring.runtimeinjectvalue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/META-INF/advance_wiring/advance_wiring.properties")
public class PropertyConfig {
    @Autowired
    private Environment env;

    @Bean
    public CompactDisc getCD(){
       String title = env.getProperty("disc.title") ;
       String artist = env.getProperty("disc.artist") ;

        return new CompactDisc(title, artist);
    }

    @Bean
    public PropertyPlaceholder getPropertyPlaceHolder(@Value("${disc.title}") String title,
                                                      @Value("${disc.artist}") String artist){
        return new PropertyPlaceholder(title, artist);
    }

//    public PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer(){
//        return new PropertySourcesPlaceholderConfigurer();
//    }
}
