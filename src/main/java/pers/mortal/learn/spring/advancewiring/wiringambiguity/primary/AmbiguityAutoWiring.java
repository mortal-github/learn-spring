package pers.mortal.learn.spring.advancewiring.wiringambiguity.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.mortal.learn.spring.advancewiring.wiringambiguity.AmbiguityExample;

@Component
public class AmbiguityAutoWiring {
    public AmbiguityExample ambiguity;

    @Autowired
    public AmbiguityAutoWiring(AmbiguityExample ambiguity){
        this.ambiguity = ambiguity;
    }

    public void look(){
        System.out.println(ambiguity.getClass().getSimpleName());
    }
}
