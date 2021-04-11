## Spring Redids 
> 在spring中使用Redis。  
> 同样可以使用模板类消除样板代码。  

**添加maven依赖**：  
```xml
<dependencies>
    <!-- Redis客户端，连接Redis -->
    <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>3.5.2</version>
    </dependency>
    <!-- Spring Data Redis,在Spring中使用模板类来使用Redis -->
    <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-redis</artifactId>
      <version>2.4.7</version>
    </dependency>
    <!-- Spring Data Redis 序列化所需要的徐丽华器实现 -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.12.2</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.12.2</version>
    </dependency>
    <dependency><!-- 序列化日期格式， 如LocalTime-->
      <groupId>com.fasterxml.jackson.datatype</groupId>
      <artifactId>jackson-datatype-jsr310</artifactId>
      <version>2.12.2</version>
    </dependency>

</dependencies>
```

**Bean配置与示例**：  
- Redis连接工厂：Spring提供四种Reids连接工厂，会生成到Reids数据库服务器的连接，即获取`ReidsConnection`。 
    - **`JedisConnectionFactory`**：常用。 
    - `JredisConnectionFactory`  
    - `LettuceConnectionFactory`    
     - `SrpConnectionFactory`   
- Reids连接：`RedisConncetion`，可以直接操作Redis命令，但是使用Redis模板更好。  
- Redis模板：Spring Data Redis 提供两个模板。
    - **添加`RedisTemplate<K,V>`Bean**: 健类型和值类型**范型化**。  
        - 默认序列化器： `JdkSerializationRedisSerializer`。    
    - **添加`SpringRedisTemplate`Bean**: StringRedisTemplate扩展了 RedisTemplate，**只关注String类型**。  
        - 默认序列化器：`StringRedis-Serializer`。  
- Redis序列化： 以下序列化器都实现了**`RedisSerializer`接口**。
    - `GenericToStringSerializer`：使用Spring转换服务进行序列化；   
    - `JacksonJsonRedisSerializer`：使用Jackson 1，将对象序 列化为JSON；   
    - `Jackson2JsonRedisSerializer`：使用Jackson 2，将对象序 列化为JSON；   
    - `JdkSerializationRedisSerializer`：使用Java序列化；   
    - `OxmSerializer`：使用Spring O/X映射的编排器和解排器 （marshaler和unmarshaler）实现序列化，用于XML序列化；   
    - `StringRedisSerializer`：序列化String类型的key和value。  
> 当某个条目保存到Redis key-value存储的时候，key和value都会使用 Redis的序列化器（serializer）进行序列化。  
```java
package pers.mortal.learn.springdata.nosql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Configuration
@ComponentScan
public class NoSqlConfiguration {

    @Bean
    @Qualifier("default")
    public RedisConnectionFactory defaultRedisConnectionFactory(){
        return new JedisConnectionFactory();//默认构造器连接localhost上的6379端口
    }

    @Bean
    @Qualifier("define")
    public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        //自定义连接的host与端头
        factory.setHostName("localhost");
        factory.setPort(6379);
        return factory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(@Qualifier("default") RedisConnectionFactory factory){
        RedisTemplate<String, String> redis = new RedisTemplate<>();
        redis.setConnectionFactory(factory);
        return redis;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(@Qualifier("default") RedisConnectionFactory factory){
        return new StringRedisTemplate(factory);//比ReidsTemlatep
    }

    /**
     * 配置 jackson mapper
     * @return
     */
    @Bean(name = "jacksonMapper")
    public ObjectMapper jacksonMapper(){
        ObjectMapper mapper=new ObjectMapper();
        //只针对非空的值进行序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 去掉各种@JsonSerialize注解的解析
        //mapper.configure(MapperFeature.USE_ANNOTATIONS, false);

        // 将类型序列化到属性json字符串中 有的文章说需要这个才能正确反序列化
        //mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 对于找不到匹配属性的时候忽略报错
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 不包含任何属性的bean也不报错
        //mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //注册它才能序列化LocalTime。
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * 关键
     * Jackson2JsonRedisSerializer 是泛型，需要指定类型
     * 这样才能正确反序列化，不然会抛出 java.util.LinkedHashMap cannot be cast YOUR OBJECT 异常
     * @return
     */
    @Bean
    public Jackson2JsonRedisSerializer<Employee> jackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Employee> jsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Employee.class);
        jsonRedisSerializer.setObjectMapper(jacksonMapper());//配置序列化映射。   
        return jsonRedisSerializer;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer(StandardCharsets.UTF_8);
    }
}


class Employee{
    public String name;
    public double salary;

    //若不在mapperObject注册JavaTimModule，则需要以下注解。
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate hiredate;

    public Employee(String name, double salary, LocalDate hiredate){
        this.name = name;
        this.salary = salary;
        this.hiredate = hiredate;
    }
}
```

**`ReidsTemplate`/`SpringReidsTemplate`操作API**：  
> RedisTemplate的很多功能是以子API的形式提供的，它们**区分了单个值和集合值**的场景。  
```java
public class StringRedisTemplate extends RedisTemplate<String, String>{}
public class RedisTemplate{
    //操作指定Redis类型的子API  
    <K,V>       ValueOperations<k, V>       opsForValue();  //操作具有简单值的条目
    <K,V>       ListOperations<K, V>        opsForList();   //操作具有list值的条目
    <K,V>       SetOperations<K, V>         opsForSet();    //操作具有set值的条目
    <K,V>       ZSetOperations<K, V>        opsForZSet();   //操作具有ZSet值（排序的set）的 条目
    <K,HK,KV>   HashOperations<K,HK,HV>     opsForHash();   //操作具有hash值的条目
    //绑定了健后再操作的子API。  
    <K,V>       BoundValueOperations<K,V>       boundValueOps(K key);       //以绑定指定key的方式，操作具有 简单值的条目
    <K,V>       BoundListOperations<K, V>       boundListOps(K key);        //以绑定指定key的方式，操作具有 list值的条目
    <K,V>       BoundSetOperations<K, V>        boundSetOps(K key);         //以绑定指定key的方式，操作具有 set值的条目
    <K,V>       BoundZSetOperations<K, V>       boundZSetOps(K key);        //以绑定指定key的方式，操作具有 ZSet值（排序的set）的条目
    <K,HK,KV>   BoundHashOperations<H,HK,HV>    boundHashOperations(K key); //以绑定指定key的方式，操作具有 hash值的条目
    //设置序列化器
    void setDefaultSerializer(RedisSerializer<?> serializer);
    void setKeySerializer(RedisSerializer<?> serializer);
    void setValueSerializer(RedisSerializer<?> serializer);
    void setHashKeySerializer(RedisSerializer<?> serializer);
    void setHasValueSerializer(RedisSerializer<?> serializer);

    void setEnableSerializer(boolean enable);
    void setStringSerializer(RedisSerializer<?> serializer);

    void afterPropertiesSet();//非Spring注入必须执行这个函数,初始化RedisTemplate
     
}
```

**`RedisTemplate`操作示例**：  
```java
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
    //直接使用RedisConnection，并测试默认连接工厂。  
    @Test
    public void testDefaultRedisConnectionFactory(){
        RedisConnection connection = defaultRedisConnectionFactory.getConnection();
        connection.set("name".getBytes(), "钟景文".getBytes());
        byte[] value = connection.get("name".getBytes());
        String string = new String(value);
        System.out.println(string);
    }
    //直接使用RedisConnection，并测试自定义连接工厂。  
    @Test
    public void testDefineRedisConnectionFactory(){
        RedisConnection connection = redisConnectionFactory.getConnection();
        connection.set("brother".getBytes(), "钟景超".getBytes());
        byte[] value = connection.get("brother".getBytes());
        String string = new String(value);
        connection.del("brother".getBytes());
    }
    //操作RedisTemplate。 
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
    //自定义序列化，示例JackJson2配置。  
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
```

