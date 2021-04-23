package pers.mortal.learn.springintegration.message;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/message/MDP.xml"})
public class MessageDrivenPOJOTest {

    @Autowired
    ActiveMQConnectionFactory activeMQConnectionFactory;

    @Autowired
    ActiveMQQueue queue;

    @Autowired
    ActiveMQTopic topic;

    @Autowired
    JmsOperations jmsOperations;

    @Test
    public void handleText() {
        int count = 10;
        for(int i=0; i<count ;i++){
            jmsOperations.convertAndSend(queue, "Queue: Message Driven POJO ————异步接收消息！");
            jmsOperations.convertAndSend(topic, "Topic: Message Driven POJO ————异步接收消息！");
        }
    }
}