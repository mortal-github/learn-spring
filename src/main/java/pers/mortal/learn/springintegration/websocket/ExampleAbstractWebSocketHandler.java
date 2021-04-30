package pers.mortal.learn.springintegration.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class ExampleAbstractWebSocketHandler extends AbstractWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession)throws Exception{
        System.out.println("Connection established");
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //处理消息
        System.out.println("Received message: " + message.getPayload());
        //模拟延时
        Thread.sleep(2000);
        //发送文本消息。
        session.sendMessage(new TextMessage("Polo!"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)throws Exception{
        System.out.println("Connection closed. Status: " + status);
    }
}
