package pers.mortal.learn.springintegration.message;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@ComponentScan
public class MessageConfiguration {

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory, ActiveMQQueue queue, ActiveMQTopic topic,
                                   MessageConverter messageConverter){
        JmsTemplate jmsTemplate = new JmsTemplate(factory);
        jmsTemplate.setDefaultDestination(queue);   //设置默认的目的地
        //jmsTemplate.setMessageConverter(messageConverter);  //默认消息转换器为SimpleMessageConverter。
        return jmsTemplate;
    }

    @Bean
    public MessageConverter messageConverter(){
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        return messageConverter;
    }

    @Bean
    public ActiveMQQueue activeMQQueue(){
        return new ActiveMQQueue("ActiveMQ.Queue");
    }

    @Bean
    public ActiveMQTopic activeMQTopic(){
        return new ActiveMQTopic("ActiveMQ.Topic");
    }

}
