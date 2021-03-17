package pers.mortal.learn.spring.advancewiring.profile;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProfileClassConfig.class)
@ActiveProfiles("prod")
public class ProfileClassConfigTest {

    @Autowired(required = false)
    private DevClass devClass;

    @Autowired(required = false)
    private ProdClass prodClass;

    @Autowired
    private AllClass allClass;

    @Test
    public void testProfile(){
        Assert.assertNull(devClass);
        Assert.assertNotNull(prodClass);
        Assert.assertNotNull(allClass);
    }
}