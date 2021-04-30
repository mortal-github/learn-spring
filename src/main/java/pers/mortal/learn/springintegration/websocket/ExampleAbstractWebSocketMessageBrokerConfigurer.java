package pers.mortal.learn.springintegration.websocket;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan
public class ExampleAbstractWebSocketMessageBrokerConfigurer extends AbstractWebSocketMessageBrokerConfigurer {

    /**
     * 注册一个 STOMP 端点。
     * 客户端在订阅或发布消息到目的地路径前，要连接到该端点。
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp_endpoints").withSockJS();
    }

    /**
     * 如果不重载它的话，将会自动配置一个简单的内存消息代理，用它来处理以"/topic"为前缀的消息。
     * 当消息到达时，目的地前缀将会决定消息该如何处理。
     * 以应用程序为目的地的消息将会直接路由到带有@MessageMapping注解的控制其方法。
     * 发送到代理上的消息，将会路由到代理上。
     * {code @MessageMapping}注解方法的返回值形成的消息也会路由到代理上。
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //基于内存的STOMP消息代理，
        //默认情况下，STOMP代理中继会假设代理监听localhost的61613端 口，并且客户端的username和password均为“guest”。
        registry.enableSimpleBroker("/queue", "/topic");

        //基于RabbitMQ的STOMP消息代理
//        registry.//启用了 STOMP代理中继（broker relay）功能，并将其目的地前缀设置 为“/topic”和“/queue”。
//                enableStompBrokerRelay("/queue", "/topic")
//                .setRelayHost("localhost")
//                .setRelayPort(61613)
//                .setClientLogin("guest")
//                .setClientPasscode("guest");

        registry.setApplicationDestinationPrefixes("/app");//设置以应用程序为目的地的消息前缀。
       // registry.setUserDestinationPrefix("/user");
    }
}

