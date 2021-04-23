package pers.mortal.learn.springintegration.message;

/**
 * Spring MDP 异步接收消息和处理消息。
 */
public class MessageDrivenPOJO {

    public void handleTextQueue(String message){
        System.out.println("ActiveMQ.Queue:" + message);
    }

    public void handleTextTopic(String message){
        System.out.println("ActiveMQ.Topic:" + message);
    }
}
