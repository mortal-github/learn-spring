package pers.mortal.learn.spring.wiringbean.javaconfig;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CDPlayerTest {
    private static AnnotationConfigApplicationContext context;
    private static CompactDisc cd;
    private static CDPlayer player;

    @Test
    public void play(){
        context = new AnnotationConfigApplicationContext(CDPlayerConfig.class);
        cd = context.getBean(CompactDisc.class);
        player = context.getBean(CDPlayer.class);

        Assert.assertNotNull(context);
        Assert.assertNotNull(cd);
        Assert.assertNotNull(player);
        player.play();
    }
}