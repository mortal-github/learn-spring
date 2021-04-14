package pers.mortal.learn.springdata.cache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableCaching
@ComponentScan
public class CacheConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

        jedisConnectionFactory.setHostName("localhost");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.afterPropertiesSet();

        return jedisConnectionFactory;
    }


    @Bean
    public RedisTemplate<Integer, CacheData> redisTemplate(RedisConnectionFactory factory,
            @Qualifier("CacheData") Jackson2JsonRedisSerializer<CacheData> jackson2JsonRedisSerializerCacheData,
            @Qualifier("Integer") Jackson2JsonRedisSerializer<Integer> jackson2JsonRedisSerializerInteger){
        RedisTemplate<Integer, CacheData> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(jackson2JsonRedisSerializerInteger);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializerCacheData);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate){
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        return cacheManager;
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
    @Qualifier("CacheData")
    public Jackson2JsonRedisSerializer<CacheData> jackson2JsonRedisSerializerCacheData(){
        Jackson2JsonRedisSerializer<CacheData> jsonRedisSerializer=new Jackson2JsonRedisSerializer<>(CacheData.class);
        jsonRedisSerializer.setObjectMapper(jacksonMapper());
        return jsonRedisSerializer;
    }

    /**
     * 关键
     * Jackson2JsonRedisSerializer 是泛型，需要指定类型
     * 这样才能正确反序列化，不然会抛出 java.util.LinkedHashMap cannot be cast YOUR OBJECT 异常
     * @return
     */
    @Bean
    @Qualifier("Integer")
    public Jackson2JsonRedisSerializer<Integer> jackson2JsonRedisSerializerInteger(){
        Jackson2JsonRedisSerializer<Integer> jsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Integer.class);
        jsonRedisSerializer.setObjectMapper(jacksonMapper());
        return jsonRedisSerializer;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer(StandardCharsets.UTF_8);
    }

}
