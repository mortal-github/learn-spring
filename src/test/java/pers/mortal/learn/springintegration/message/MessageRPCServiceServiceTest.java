package pers.mortal.learn.springintegration.message;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {MessageConfiguration.class})
@ContextConfiguration(locations = {"classpath:/META-INF/message/RPCService.xml"})
public class MessageRPCServiceServiceTest {

    @Autowired
    private ActiveMQConnectionFactory connectionFactory;

    @Autowired
    private MessageRPCServiceImpl messageRPCService;

    @Autowired
    private JmsInvokerServiceExporter jmsInvokerServiceExporter;

    @Test
    public void sendMessage() {
        int i = 0;
        int j = i;//暂停主线程是为了让远程服务保持活动。
    }
}