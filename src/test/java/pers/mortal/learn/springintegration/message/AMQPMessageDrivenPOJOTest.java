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
public class AMQPMessageDrivenPOJOTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void handleQueue() {
        String[] names = new String[]{
                "Rabbit.Queue.1",
                "Rabbit.Queue.2",
                "Rabbit.Queue.3"
        };
        for(String name : names){
            rabbitTemplate.convertAndSend("direct", name,name+"消息转换的String");
            Message message  = new Message(("字节数组的Spring").getBytes(), new MessageProperties());
            rabbitTemplate.send("direct", name ,message);
        }
    }
}