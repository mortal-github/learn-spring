package pers.mortal.learn.spring.advancewiring.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProfileClassConfigProd {
    @Bean
    public ProdClass getProdClass(){ return new ProdClass(); }

}
