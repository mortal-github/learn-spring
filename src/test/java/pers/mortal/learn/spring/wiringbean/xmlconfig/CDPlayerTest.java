package pers.mortal.learn.spring.wiringbean.xmlconfig;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CDPlayerTest {
    private static ClassPathXmlApplicationContext context;
    private static CompactDisc cd;
    private static CDPlayer player;
    private static BlankDisc bcd;

    @Test
    public void play(){
        context = new ClassPathXmlApplicationContext("/META-INF/wiring_bean/wiring_bean.xml");
        cd = context.getBean(SgtPeppers.class);
        player = context.getBean(CDPlayer.class);
        bcd = context.getBean(BlankDisc.class);

        Assert.assertNotNull(context);
        Assert.assertNotNull(cd);
        Assert.assertNotNull(player);
        Assert.assertNotNull(bcd);
        player.play();
    }
}