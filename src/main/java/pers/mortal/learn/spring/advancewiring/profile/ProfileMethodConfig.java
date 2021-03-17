package pers.mortal.learn.spring.advancewiring.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileMethodConfig {

    @Bean
    @Profile("prod")
    public ProdClass getProdClass(){
        return new ProdClass();
    }

    @Bean
    @Profile("dev")
    public DevClass getDevClass(){
        return new DevClass();
    }

    @Bean
    public AllClass getAllClass(){return new AllClass();}

}
