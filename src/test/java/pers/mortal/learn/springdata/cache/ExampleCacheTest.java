package pers.mortal.learn.springdata.cache;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CacheConfiguration.class})
public class ExampleCacheTest {
    private static int id = 0;

    @Autowired
    ExampleCache exampleCache;

    @Test
    public void test(){

        CacheData cacheData;
        cacheData = exampleCache.add(new CacheData(true, true));
        cacheData = exampleCache.get(1);
        cacheData = exampleCache.get(1);
        cacheData = exampleCache.add(new CacheData(true, false));
        cacheData = exampleCache.get(2);
        cacheData = exampleCache.get(2);
        cacheData = exampleCache.add(new CacheData(false, true));
        cacheData = exampleCache.get(3);
        cacheData = exampleCache.get(3);
        cacheData = exampleCache.add(new CacheData(false, false));
        cacheData = exampleCache.get(4);
        cacheData = exampleCache.get(4);

        cacheData = exampleCache.remove(1);
        cacheData = exampleCache.get(1);
        cacheData = exampleCache.remove(2);
        cacheData = exampleCache.get(2);
        cacheData = exampleCache.remove(3);
        cacheData = exampleCache.get(3);
        cacheData = exampleCache.remove(4);
        cacheData = exampleCache.get(4);

        exampleCache.clear();

        System.out.println("结束");
    }


}