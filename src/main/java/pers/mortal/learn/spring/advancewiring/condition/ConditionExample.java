package pers.mortal.learn.spring.advancewiring.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ConditionExample implements Condition {
    public static boolean condition = false;
    static{
        if(Math.random() > 0.5){
            condition = true;
        }
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return condition;
    }
}
