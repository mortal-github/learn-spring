package pers.mortal.learn.springintegration.message;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {MessageConfiguration.class})
@ContextConfiguration(locations = {"classpath:/META-INF/message/RPCClient.xml"})
public class MessageRPCServiceClientTest {

    @Autowired
    private ActiveMQConnectionFactory connectionFactory;

    @Autowired
    private MessageRPCService service;

    @Test
    public void sendMessage() {
        connectionFactory.setTrustAllPackages( true );//ActiveMQ必须设置白名单的类，才能被传输。https://blog.csdn.net/u014396256/article/details/80594135
        for(int i=1; i<=10; i++){
            service.sendMessage(i + ". 基于消息的RPC");
        }
    }
}