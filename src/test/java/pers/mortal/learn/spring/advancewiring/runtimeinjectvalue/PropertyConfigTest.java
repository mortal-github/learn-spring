package pers.mortal.learn.spring.advancewiring.runtimeinjectvalue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PropertyConfig.class})
public class PropertyConfigTest {
    @Autowired
    private CompactDisc cd;

    @Autowired
    private PropertyPlaceholder holder;

    @Test
    public void testInjectValue(){
        Assert.assertEquals("CD TITLE", cd.getTitle());
        Assert.assertEquals("CD ARTIST", cd.getArtist());

        Assert.assertEquals("CD TITLE", holder.getTitle());
        Assert.assertEquals("CD ARTIST", holder.getArtist());
    }
}