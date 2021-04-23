package pers.mortal.learn.springintegration.message;

public class AMQPMessageDrivenPOJO {
    public void handleQueue(String message){
        System.out.println("Rabbit.Queue:" + message);
    }
}
