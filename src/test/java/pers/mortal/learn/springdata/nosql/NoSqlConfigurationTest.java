package pers.mortal.learn.springdata.nosql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {NoSqlConfiguration.class})
public class NoSqlConfigurationTest {

    @Autowired
    @Qualifier("default")
    private RedisConnectionFactory defaultRedisConnectionFactory;

    @Autowired
    @Qualifier("define")
    private  RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StringRedisSerializer stringRedisSerializer;

    @Autowired
    private Jackson2JsonRedisSerializer<Employee> jackson2JsonRedisSerializer;

    @Test
    public void testDefaultRedisConnectionFactory(){
        RedisConnection connection = defaultRedisConnectionFactory.getConnection();
        connection.set("name".getBytes(), "钟景文".getBytes());
        byte[] value = connection.get("name".getBytes());
        String string = new String(value);
        System.out.println(string);
    }

    @Test
    public void testDefineRedisConnectionFactory(){
        RedisConnection connection = redisConnectionFactory.getConnection();
        connection.set("brother".getBytes(), "钟景超".getBytes());
        byte[] value = connection.get("brother".getBytes());
        String string = new String(value);
        connection.del("brother".getBytes());
    }

    @Test
    public void testRedisTemplateOrStringRedisTemplate(){
//        RedisTemplate<String, String> template = redisTemplate;//生成的健会有转码，因为使用了默认序列化器。
        StringRedisTemplate template = stringRedisTemplate;//生成的健不会有转码
        template.opsForHash().put("h", "hk", "hv");
        template.opsForHash().put("h", "hk1", "hv1");
        Map<Object, Object> list = template.opsForHash().entries("h");
        template.delete("h");

        template.opsForValue().set("name", "钟景文");
        template.opsForValue().set("brother", "钟景超");
        template.delete("name");
        template.delete("brother");

        template.opsForList().rightPush("list", "elem1");
        template.opsForList().leftPush("list", "elem2");
        template.delete("list");

        template.opsForSet().add("set", "val1");
        template.opsForSet().add("set", "val2");
        template.delete("set");

        BoundZSetOperations<String, String> zset = template.boundZSetOps("zset");
        zset.add("num1", 1);
        zset.add("num0", 0);
        zset.add("num3", 3);
        template.delete("zset");
    }

    @Test
    public void testSerializer(){
      RedisTemplate<String, Employee> template = new RedisTemplate<>();
      template.setConnectionFactory(defaultRedisConnectionFactory);

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        /**非Spring注入必须执行这个函数,初始化RedisTemplate*/
        template.afterPropertiesSet();

        Employee wen = new Employee("zhongJingwen", 14144.34, LocalDate.now());
        Employee chao = new Employee("zhongJingchoa", 10000.02, LocalDate.now().minusYears(10));
        template.opsForValue().set("web", wen);
        template.opsForValue().set("chao", chao);
        template.delete("web");
        template.delete("chao");
    }
}
