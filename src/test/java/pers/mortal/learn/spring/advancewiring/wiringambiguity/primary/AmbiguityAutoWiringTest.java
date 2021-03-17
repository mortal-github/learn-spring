package pers.mortal.learn.spring.advancewiring.wiringambiguity.primary;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.mortal.learn.spring.advancewiring.wiringambiguity.AmbiguityExample;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AmbiguityConfig.class})
public class AmbiguityAutoWiringTest {

        @Autowired
        private AmbiguityAuto auto;

        @Autowired
        private AmbiguityConfig  config;

        @Autowired
        private AmbiguityXML xml;

        @Autowired
        private AmbiguityAutoWiring wiring;

        @Test
        public void testPrimary(){
            Assert.assertNotNull(auto);
            Assert.assertNotNull(config);
            Assert.assertNotNull(xml);
            Assert.assertNotNull(wiring);

            wiring.look();
        }
    }