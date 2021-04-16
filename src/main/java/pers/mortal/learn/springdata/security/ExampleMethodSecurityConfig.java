package pers.mortal.learn.springdata.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true//如果securedEnabled属 性的值为true的话，将会创建一个切点，这样的话Spring Security切 面就会包装带有@Secured注解的方法。
        ,jsr250Enabled = true  //true 则开启`@RolesAllowed`注解。
        ,prePostEnabled = true
)
public class ExampleMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    //Web安全的配置类扩展了 WebSecurityConfigurerAdapter，与之类似，这个类能够为方 法级别的安全性提供更精细的配置。
    //例如，如果我们在Web层的安全配置中设置认证，那么可以通过重 载GlobalMethodSecurityConfiguration的configure()方 法实现该功能

    //注册许可计算器
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler(){
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new ExamplePermissionEvaluator());
        return expressionHandler;
    }
}
