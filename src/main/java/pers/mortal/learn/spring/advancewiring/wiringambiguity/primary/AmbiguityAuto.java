package pers.mortal.learn.spring.advancewiring.wiringambiguity.primary;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pers.mortal.learn.spring.advancewiring.wiringambiguity.AmbiguityExample;

@Component
//@Primary
public class AmbiguityAuto implements AmbiguityExample {
}
