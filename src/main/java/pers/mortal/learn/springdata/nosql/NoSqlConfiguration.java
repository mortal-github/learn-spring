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
        return new StringRedisTemplate(factory);
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
        jsonRedisSerializer.setObjectMapper(jacksonMapper());
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