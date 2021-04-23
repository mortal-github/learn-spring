package pers.mortal.learn.springintegration.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/message/AMQP.xml"})
public class AMQPTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testRabbitTemplateSend(){
        //它传入了三个参数：Exchange的名称、routing key以及要发送的对 象。注意，这里并没有指定消息该路由到何处、要发送给哪个队列以 及期望哪个消费者来获取消息。
        rabbitTemplate.convertAndSend("fanout", "Rabbit.Queue", "不知道消息被传递到哪了");

        //RabbitTemplate有多个重载版本的convertAndSend()方法，这 些方法可以简化它的使用。
        //如果在参数列表中省略Exchange名称，或者同时省略Exchange名称和 routing key的话，RabbitTemplate将会使用默认的Exchange名称和 routing key。
        //默认的Exchange名称为空,默认的routing key也为空。
        //可以在<template>元素上借助exchange和routing- key属性配置不同的默认值：
        rabbitTemplate.convertAndSend("Rabbit.Queue", "省略Exchange");
        rabbitTemplate.convertAndSend("省略Exchange和RoutinKey");

        //们可以使用较低等级的send()方法来发 送org.springframework.amqp.core.Message对象
        Message lowerMessage = new Message("低级send方法".getBytes(), new MessageProperties());
        rabbitTemplate.send("fanout", "Rabbit.Queue", lowerMessage);

    }

    @Test
    public void testRabbitTemplateReceive(){
        //借助receive()方法，我们可 以从队列中获取一个Message对象
        Message message1 = rabbitTemplate.receive("Rabbit.Queue.1");
        //还可以配置获取消息的默认队列，这是通过 在配置模板的时候，设置queue属性实现的。
        Message message2 = rabbitTemplate.receive();
        //在获取到Message对象之后，我们可能需要将它body属性中的字节 数组转换为想要的对象。
        String string1 = new String(message1.getBody());
        String string2 = new String(message1.getBody());
        //可以考虑使用RabbitTemplate的 receiveAndConvert()方法作为替代方案。
        String string3 = (String) rabbitTemplate.receiveAndConvert("Rabbit.Queue.1");
        String string4 = new String((byte[])rabbitTemplate.receiveAndConvert());//使用默认队列名。
        string4.charAt(0);
    }
}