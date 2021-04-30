package pers.mortal.learn.springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pers.mortal.learn.springdata.security.ExampleMethodSecurityConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = {RootConfig.class, ExampleMethodSecurityConfig.class},
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ANNOTATION, value= EnableWebMvc.class)
        })
public class RootConfig {

        @Bean
        public SpittleRepository getSpittleRepository(){
                return new SpittleRepository() {
                        @Override
                        public List<Spittle> findSpittles(long max, int count) {
                                if(max == Long.MAX_VALUE){
                                        max = count ;
                                }
                                if(max < count){
                                        count = (int)max;
                                }
                                long offset = max - count;
                                List<Spittle> spittles = new ArrayList<>();
                                for(int i=1; i <=count; i++){
                                        spittles.add(new Spittle("Spittle " + (i + offset), new Date()));
                                }
                                return spittles;
                        }
                        @Override
                        public Spittle findOne(long id){
                                return new Spittle("Spittle " + id, new Date());
                        }
                };
        }

        @Bean
        public SpitterRepository getSpitterRepository(){
                return new SpitterRepository() {
                        List<Spitter> spitters = new ArrayList<>();
                        @Override
                        public void save(Spitter spitter) {
                                spitters.add(spitter);
                        }

                        @Override
                        public Spitter findByUserName(String userName) {
                               for(Spitter spitter : spitters){
                                       if(spitter.getUserName().equals(userName)){
                                               return spitter;
                                       }
                               }
                               return null;
                        }
                };
        }
}
