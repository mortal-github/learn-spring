package pers.mortal.learn.spring.advancewiring.condition;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConditionalComponentConfig.class, ConditionalBeanConfig.class})
public class ConditionalTest {
    @Autowired(required = false)
    private ConditionalComponent component;

    @Autowired(required = false)
    private ConditionalBean bean;

    @Test
    public void testCondition(){
        if(ConditionExample.condition){
            Assert.assertNotNull(component);
            Assert.assertNotNull(bean);
        }else {
            Assert.assertNull(component);
            Assert.assertNull(bean);
        }
        System.out.println(ConditionExample.condition);
    }

}