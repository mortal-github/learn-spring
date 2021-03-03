package pers.mortal.learn.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnightConfig {//声明一个配置类

    //对每一个bean声明一个方法来构造bean对象。
    @Bean
    public Knight knight() {
        return new BraveKnight(quest());
    }

    //对每一个bean声明一个方法来构造bean对象。
    @Bean
    public Quest quest() {
        return new SlayDragonQuest(System.out);
    }
}
