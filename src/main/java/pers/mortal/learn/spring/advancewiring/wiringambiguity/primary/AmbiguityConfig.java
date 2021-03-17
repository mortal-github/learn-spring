package pers.mortal.learn.spring.advancewiring.wiringambiguity.primary;

import org.springframework.context.annotation.*;
import pers.mortal.learn.spring.advancewiring.wiringambiguity.AmbiguityExample;

@Configuration
@ComponentScan
@ImportResource("/META-INF/advance_wiring/advance_wiring.xml")
public class AmbiguityConfig {
    @Bean
    @Primary
    public AmbiguityExample getAmbiguityJava(){
        return new AmbiguityJava();
    }
}
