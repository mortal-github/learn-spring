package pers.mortal.learn.spring.advancewiring.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class ScopeConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PrototypeScope getPrototypeScope(){
        return new PrototypeScope();
    }

    @Bean
    @Scope(
            value = WebApplicationContext.SCOPE_REQUEST,
            proxyMode = ScopedProxyMode.INTERFACES
    )
    public SessionScope getSessionScope(){
        return null;
    }

    @Bean
    @Scope(
            value = WebApplicationContext.SCOPE_SESSION,
            proxyMode = ScopedProxyMode.TARGET_CLASS// RequestScope是类，是无法建立基于接口的代理，需要使用CGLib生成基于类的代理。
    )
    public RequestScope getRequestScope(){
        return new RequestScope();
    }

}
