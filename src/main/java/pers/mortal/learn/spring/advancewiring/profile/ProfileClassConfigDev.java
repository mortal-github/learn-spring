package pers.mortal.learn.spring.advancewiring.profile;

import org.springframework.context.annotation.*;

@Configuration
@Profile("dev")
public class ProfileClassConfigDev {

    @Bean(name="development_environment")
    public DevClass getDevClass(){ return new DevClass(); }

}
