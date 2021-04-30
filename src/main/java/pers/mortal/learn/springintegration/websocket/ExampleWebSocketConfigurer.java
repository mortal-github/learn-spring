package pers.mortal.learn.springintegration.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class ExampleWebSocketConfigurer implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(exampleAbstractWebSocketHandler(), "/websocket")
                .withSockJS();
    }

    @Bean
    public ExampleAbstractWebSocketHandler exampleAbstractWebSocketHandler(){
        return new ExampleAbstractWebSocketHandler();
    }
}
