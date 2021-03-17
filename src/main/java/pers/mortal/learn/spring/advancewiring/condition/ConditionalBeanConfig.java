package pers.mortal.learn.spring.advancewiring.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConditionalBeanConfig {
    @Bean
    @Conditional(ConditionExample.class)
    public ConditionalBean getConditionalBean(){
        return new ConditionalBean();
    }
}
