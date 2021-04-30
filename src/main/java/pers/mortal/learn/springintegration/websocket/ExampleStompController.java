package pers.mortal.learn.springintegration.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ExampleStompController {

    /** 通过AnnotationMethodMessageHandler接收消息
     *  即处理发往/app/message的消息
     */
    @MessageMapping("/message")
    public void handle(Shout incoming){
        System.out.println(incoming.getMessage());
    }

    /**
     * 当收到STOMP订阅消息的时候，带有@SubscribeMapping注 解的方法将会触发。
     * 通过AnnotationMethodMessageHandler接收消息。
     * {code @SubscribeMapping}的主要应用场景是实现请求-回应模式。在请求-回应模式中，客户端订阅某一个目的地，然后预期在这个目的地 上获得一个一次性的响应。
     * 这里的关键区别在于 HTTP GET请求是同步的，而订阅的请求-回应模式则是异步的，这样 客户端能够在回应可用时再去处理，而不必等待。
     * @return
     */
    @SubscribeMapping("/subscribe")
    public Shout subscribe(){
        Shout shout = new Shout();
        shout.setMessage("订阅了一个消息");
        return shout;
    }


    /**
     * 处理消息后发发送消息。
     * 默认情况下，帧所发往的目的地会与触发处理器方法的目的地相同， 只不过会添加上“/topic”前缀。
     * 可以通过为方法添 加@SendTo注解，重载目的地，所有订阅这 个主题的应用（如客户端）都会收到这条消息。
     *
     * @param incoming
     */
    @MessageMapping("/message_send")
    @SendTo("/topic/shout")
    public Shout handleSend(Shout incoming){
        return incoming;
    }

    /**
     * 按照类似的方式，@SubscribeMapping注解标注 的方式也能发送一条消息，作为订阅的回应。
     * {code @SubscribeMapping}的区别在于这里的Shout消息将会直接发送 给客户端，而不必经过消息代理。
     * 如果你为方法添加@SendTo注解 的话，那么消息将会发送到指定的目的地，这样会经过代理。
     * @return
     */
    @SubscribeMapping("/subscribe_send")
    public Shout subscribe_send(){
        Shout shout = new Shout();
        shout.setMessage("订阅了一个消息");
        return shout;
    }


}
