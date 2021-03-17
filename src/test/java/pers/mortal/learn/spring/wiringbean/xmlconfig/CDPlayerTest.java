package pers.mortal.learn.spring.wiringbean.xmlconfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/wiring_bean/wiring_bean.xml"})
public class CDPlayerTest {
    @Autowired
    private SgtPeppers cd;
    
    @Autowired
    private CDPlayer player;

    @Autowired
    private BlankDisc bcd;

    @Test
    public void play(){
        Assert.assertNotNull(cd);
        Assert.assertNotNull(player);
        Assert.assertNotNull(bcd);
        player.play();
    }
}