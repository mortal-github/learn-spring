package pers.mortal.learn.spring.advancewiring.wiringambiguity.qualifier;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AmbiguityConfig.class})
public class AmbiguityAutoWiringTest {

    @Autowired
    private AmbiguityAutoWiring wiring;

    @Test
    public void testQualifier(){
        Assert.assertTrue(wiring.cold instanceof AmbiguityCold);
        Assert.assertTrue(wiring.def instanceof AmbiguityDefault);
        Assert.assertTrue(wiring.annotate instanceof AmbiguityAnnotate);
    }
}