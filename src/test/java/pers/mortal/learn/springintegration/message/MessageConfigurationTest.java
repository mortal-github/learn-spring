package pers.mortal.learn.springintegration.message;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {MessageConfiguration.class})
@ContextConfiguration(locations = {"classpath:/META-INF/message/MDP.xml"})
public class MessageConfigurationTest {
    @Autowired
    private ActiveMQConnectionFactory connectionFactory;

    @Autowired
    private JmsOperations jmsOperations;//jmsTemplate实现的接口

    @Autowired
    private ActiveMQQueue queue;

    @Autowired
    private ActiveMQTopic topic;

    @Autowired
    private MappingJackson2MessageConverter converter;

    @Test
    public void testJMS(){
        Destination destination = topic;
        Thread receive = new Thread(()->{testJMSReceiveMessage(destination);});
        Thread send = new Thread(()->{testJMSSendMessage(destination);});

        if(destination == queue){
            send.start();
            receive.start();
        }else  if(destination == topic){
            receive.start();
            send.start();
        }
    }

    private void testJMSSendMessage(Destination destination) {
        Connection connection = null;
        Session session = null;
        try{
            //连接会话并获取发送者。
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            //发送消息
            TextMessage message = session.createTextMessage();
            message.setText("Hello World");
            producer.send(message);

        }catch(JMSException e){
            e.printStackTrace();
        }finally{
            try{
                if(null != session){
                    session.close();
                }
                if(null != connection){
                    connection.close();
                }
            }catch(JMSException ex){
                ex.printStackTrace();
            }
        }
    }

    private void testJMSReceiveMessage(Destination destination){
        Connection connection = null;
        Session session = null;
        try{
            //连接会话并获取接收者。
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);
            //接受消息
            connection.start();
            Message message = consumer.receive();
            TextMessage textMessage = (TextMessage)message;
            int i =0;
        }catch(JMSException e){
            e.printStackTrace();
        }finally{
            try{
                if(null != session){
                    session.close();
                }
                if(null != connection){
                    session.close();
                }
            }catch(JMSException ex){
                ex.printStackTrace();
            }
        }

    }

    @Test
    public void testJmsTemplate(){
        testJmsTemplateSendMessage();
        testJmsTemplateReceiveMessage();
    }

    private void testJmsTemplateSendMessage(){
        //第一个参数是消息目的地
        //第二个参数是MessageCreator,在createMessage()的方法中通过Session创建一个(对象)消息。
        jmsOperations.send(queue,
                new MessageCreator(){
                    public Message createMessage(Session session)throws JMSException{
                        return session.createObjectMessage("Hello world!");
                    }
                });
        //设置了默认目的地后，可以省略第一次参数
        jmsOperations.send(new MessageCreator(){
            public Message createMessage(Session session)throws JMSException{
                return session.createTextMessage("Welcome to ActiveMQ!");
            }
        });
        //使用消息转换器(MessageConverter)，则不需要MessageCreator。
        //convertAndSend()会使用内置的消息转换器为我们创建消息。
        jmsOperations.convertAndSend("Bye Bye!");
    }

    private void testJmsTemplateReceiveMessage(){
        ObjectMessage hello   = (ObjectMessage) jmsOperations.receive(queue);
        TextMessage welcome = (TextMessage) jmsOperations.receive();
        Object bye      = jmsOperations.receiveAndConvert();

        ///JmsTemplate无法处理调用ObjectMessage的getObject()方法。
        //因为这不是调用JmsTemplate的方法。
        //可以使用Spring的JmsUtils的convertJmsAccessException()方法把检查型异常转化为非检查型异常。
        try{

            String helloWord = (String) hello.getObject();
            String welcomeTo = welcome.getText();
            int i = 0;
        }catch(JMSException jmsException){
            throw JmsUtils.convertJmsAccessException(jmsException);//转化为非检查异常。
        }

    }

    @Test
    public void testActiveMQQueue() {
    }

    @Test
    public void testActiveMQTopic() {
    }
}