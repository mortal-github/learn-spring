package pers.mortal.learn.springintegration.message;

import org.springframework.stereotype.Component;

@Component("MessageRPCService")
public class MessageRPCServiceImpl implements MessageRPCService {

    public void sendMessage(final String string){
        System.out.println(string);
    }
}

