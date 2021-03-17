package pers.mortal.learn.spring.advancewiring.condition;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {ConditionalComponent.class})
public class ConditionalComponentConfig {
}
