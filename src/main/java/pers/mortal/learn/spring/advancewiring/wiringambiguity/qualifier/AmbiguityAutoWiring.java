package pers.mortal.learn.spring.advancewiring.wiringambiguity.qualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pers.mortal.learn.spring.advancewiring.wiringambiguity.AmbiguityExample;

@Component
public class AmbiguityAutoWiring {
    @Autowired
    @Qualifier("cold")
    public AmbiguityExample cold;

    @Autowired
    @Qualifier("ambiguityDefault")
    public AmbiguityExample def;

    @Autowired
    @Cream
    @Cold
    public AmbiguityExample annotate;

    public void look(){
        System.out.println(cold.getClass().getSimpleName());
        System.out.println(def.getClass().getSimpleName());
        System.out.println(annotate.getClass().getSimpleName());
    }
}
