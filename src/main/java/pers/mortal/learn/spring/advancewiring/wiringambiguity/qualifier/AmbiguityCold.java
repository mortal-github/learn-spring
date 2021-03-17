package pers.mortal.learn.spring.advancewiring.wiringambiguity.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pers.mortal.learn.spring.advancewiring.wiringambiguity.AmbiguityExample;

@Component
@Qualifier("cold")
public class AmbiguityCold implements AmbiguityExample {
}
