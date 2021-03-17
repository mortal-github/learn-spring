package pers.mortal.learn.spring.advancewiring.condition;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(ConditionExample.class)
public class ConditionalComponent {
}
