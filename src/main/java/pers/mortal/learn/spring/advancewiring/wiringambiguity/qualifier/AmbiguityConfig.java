package pers.mortal.learn.spring.advancewiring.wiringambiguity.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import pers.mortal.learn.spring.advancewiring.wiringambiguity.AmbiguityExample;

@Configuration
@ComponentScan
@ImportResource("/META-INF/advance_wiring/advance_wiring.xml")
public class AmbiguityConfig {

    @Bean(name = "ambiguityDefault")
    @Qualifier
    public AmbiguityDefault getAmbiguityDefault(){
        return new AmbiguityDefault();
    }

    @Bean
    @Cold
    @Cream
    public AmbiguityAnnotate getAmbiguityAnnotate(){
        return new AmbiguityAnnotate();
    }
}
