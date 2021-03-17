package pers.mortal.learn.spring.advancewiring.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ProfileClassConfigDev.class, ProfileClassConfigProd.class})
public class ProfileClassConfig {

    @Bean
    public AllClass getAllClass(){ return new AllClass(); }
}
