package pers.mortal.learn.spring.wiringbean.auto;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan("pers.mortal.learn.spring.wiringbean")
//@ComponentScan(basePackages={"pers.mortal.learn.spring.wiringbean"})
@ComponentScan(basePackageClasses={CDPlayer.class})
public class CDPlayerConfig {

}
