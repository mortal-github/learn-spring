## Spring Data Cache  
> 缓存（Caching）可以存储经常会用到的信息，这样每次需要的时 候，这些信息都是立即可用的。  
> 尽管Spring自身并没有实现缓存解决方案，但是它对缓存 功能提供了声明式的支持，能够与多种流行的缓存实现进行集成。  

### 启用对缓存的支持  

**Spring对缓存的支持有两种方式**：   
- **注解驱动**的缓存：    
- **XML声明**的缓存： 

**启动Spring对注解缓存的支持*: 
> 在往bean上添加缓存注解之前，必须要启用Spring对注解驱动缓存的支持。  
> 然后声明缓存管理器。  
- **`@EnableCaching`**:  如果我们使用Java配置的话，那么可以在其中的一个配置类 上添加@EnableCaching。  
- **`<cache:annotation-driven>`**: 如果以XML的方式配置应用的话，那么可以使用**Spring cache命名空 间中**的`<cache:annotation-driven>`元素来启用注解驱动的缓存。   
```java
@Configuration
@EnableCaching
public class CachingConfig{
    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager();
    }
}
```
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:cache="http://www.springframework.org/schema/cache"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
                        http://www.springframework.org/schema/beans/spring-beans.xsd 
                        http://www.springframework.org/schema/cache 
                        http://www.springframework.org/schema/cache/spring-cache.xsd">
    <cache:annotation-driven/>
    <bean id="cacheManager" class="org.springframework.cache.concurrent.ConcurrentMapCacheManager"/>
</bean>
```

**缓存原理**： 
> 其实在本质上，@EnableCaching和<cache:annotation- driven>的工作方式是相同的。  
> 它们都会创建一个切面（aspect）并 触发Spring缓存注解的切点（pointcut）。  
> 根据所使用的注解以及缓存 的状态，这个切面会从缓存中获取数据，将数据添加到缓存之中或者 从缓存中移除某个值。   

#### 缓存管理器  
**内置缓存管理器**： 
> 缓存管理器是Spring缓存抽象的核心，它能够与多 个流行的缓存实现进行集成。  
> Spring 3.1内置了五个缓存管理器实现。  
- `SimpleCacheManager`:  
- `NoOpCacheManager`:  
- `ConcurrentMapCacheManager`:  
- `CompositeCacheManager`:  
- `EhCacheCacheManager`:  

**JCache缓存管理**：  
> Spring 3.2引入了另外一个缓存管理器，这个管理器可以用在基于 JCache（JSR-107）的缓存提供商之中。  

**`Spring Data`缓存管理**： 
> 除了核心的Spring框架， Spring Data又提供了两个缓存管理器。  
- `RedisCacheManager`: 来自于Spring Data Redis项目。  
- `GemfireCacheManager`: 来自于Spring Data GemFire项目。  

#### 配置缓存管理  
> 我们必须选择一个缓存管理器，然后要在Spring应用上下文中，以 bean的形式对其进行配置。  

**使用Ehcache缓存**：  
> Ehcache是最为流行的缓存供应商之一。  
> 注意Spring和EhCache都定义了CacheManager 类型。  
> 需要明确的是，EhCache的CacheManager要被注入到Spring 的EhCacheCacheManager（Spring CacheManager的实现）之中。   
> Spring提供了 EhCacheManager-FactoryBean来生成EhCache的 CacheManager。  
> 除了在Spring中配置的bean，还需要有针对EhCache的配置，通过调用setConfigLocation()方 法，传入ClassPath-Resource，用来指明EhCache XML配置文件。    
```java
import net.sf.ehcache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;import org.springframework.core.io.ClassPathResource;

@Configuration 
@EnableCaching 
public class CachingConfig{
    //配置EhCacheCacheManager
    @Bean
    public EhCacheCacheManager cacheManager(CacheManager cm){
        return new EhCacheCacheManager(cm);
    }
    //配置EhCacheManagerFactoryBean
    @Bean
    public EhCaCheManagerFactoryBean ehcache(){
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("xxx/yyy/ehcache.xml"));//配置Ehcache配置文件的路径。 
    }
}
```

**Echache配置**： 
> EhCache 为XML定义了自己的配置模式，我们需要在一个XML文件中配置缓 存，该文件需要符合EhCache所定义的模式。  
> 至于ehcache.xml文件的内容，不同的应用之间会有所差别，但是至少 需要声明一个最小的缓存。  
> 例如，如下的EhCache配置声明一个名 为spittleCache的缓存，它最大的堆存储为50MB，存活时间为 100秒。  
```xml
<ehcache>
    <cache name="spittleCache"
            maxBytesLocalHeap="50m"
            timeToLiveSeconds="100"/>
</ehcache>
```

**使用Redis缓存**：  
> RedisCacheManager会与一个Redis服务器协作，并通过 RedisTemplate将缓存条目存储到Redis中。  
> 为了使用RedisCacheManager，我们需要RedisTemplate bean以 及RedisConnectionFactory实现类。  
```java
@Configuration 
@EnableCaching 
public class CachingConfig{
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate){
        return new RedisCacheManager(redisTemplate);
    }
    
    @Bean
    public JedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }
    
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
```

**使用多个缓存**：  
> 我们并不是只能有且仅有一个缓存管理器。  
> 如果你很难确定该使用哪 个缓存管理器，或者有合法的技术理由使用超过一个缓存管理器的话，  
> 那么可以尝试使用Spring的CompositeCacheManager。  
> `CompositeCacheManager`要通过一个或更多的缓存管理器来进行配置，它会迭代这些缓存管理器，以查找之前所缓存的值。   
```java
@Configuration 
@EnableCaching 
public class CachingConfig{
    @Bean
    public CacheManager cacheManager(net.sf.ehcache.CacheManager cm,
                                      javax.cache.CacheManager jcm){
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        //多缓存管理器列表  
        List<CacheManager> managers = new ArrayList<>();
        managers.add(new JCacheCacheManager(jcm)) ;
        managers.add(new EhCacheCacheManager(cm));
        managers.add(new RedisCacheManager(redisTemplate));
        //添加迭代用的多个换取管理器。 
        cacheManager.setCacheManagers(managers);
        //当查找缓存条目时，CompositeCacheManager首先会从 JCacheCacheManager开始检查JCache实现，  
        // 然后通过 EhCacheCacheManager检查Ehcache，   
        // 最后会使 用RedisCacheManager来检查Redis，完成缓存条目的查找。  
    }
                   
}
```

### 为方法添加注解以支持缓存  
> 在配置完缓存管理器并启用缓存后，就可以在bean方法上应用缓存规 则了。  

**缓存注解API**：  
> 所有缓存注解都能运用再方法或类上。  
> 可以考虑将注解添加到接口的方法声明上，而不是放在实现类中，这些实现类都会应用相同 的缓存规则。  
> 默认的缓存key要基于方法的参数来确定。 
- 用在方法上： 注解所描述的缓存行为只会运用到这个方法上。  
- 用在类上：  缓存行为就会应用到这个类的所有方法上。  
```java
/**
* 表明Spring在调用方法之前，首先应该在缓存中查找方法的返回值。  
* 如果这个值能够找到，就会返回缓存的值。  
* 否则的话，这个方法就 会被调用，返回值会放到缓存之中。  
*/
public @interface Cacheable{ 
    String[] value();       //要使用的缓存名称。  
    String key();           //SpEL表达式，用来计算自定义的缓存key。  
    String condition();     //SpEL表达式。如果为false，不会将缓存应用到方法调用上，即无法添加缓存和查询换取。 
    String unless();        //SpEL表达式，如果得到的值是true的话，返回值不会放到缓 存之中，但是可以查询（与condition的区别再次）。  
}
/**
* 表明Spring应该将方法的返回值放到缓存中。 
* 在方法的调用前并不会 检查缓存，方法始终都会被调用。 
*/
public @interface  CachePut{
    String[] value();       //要使用的缓存名称。  
    String key();           //SpEL表达式，用来计算自定义的缓存key。  
    String condition();     //SpEL表达式。如果为false，不会将缓存应用到方法调用上，即无法添加缓存和查询换取。 
    String unless();        //SpEL表达式，如果得到的值是true的话，返回值不会放到缓 存之中，但是可以查询（与condition的区别再次）。  
}
/**
* 表明Spring应该在缓存中清除一个或多个条目
*/
public @interface CacheEvict{
    String[] value();       //要使用的缓存名称。  
    String key();           //SpEL表达式，用来计算自定义的缓存key。  
    String condition();     //SpEL表达式。如果为false，不会将缓存应用到方法调用上，即无法添加缓存和查询换取。 
    boolean allEntries();   //如果为true的话，特定缓存的所有条目都会被移除掉。  
    boolean beforeInvocation(); //如果为true的话，在方法调用之前移除条目。如果 为false（默认值）的话，在方法成功调用之后再移除 条目。  
}
/**
*这是一个分组的注解，能够同时应用多个其他的缓存注解
*/
public @interface Caching{

}
```

**Spring的用于定义缓存规则的SpEL扩展**：  
> 在为缓存编写SpEL表达式的时候，Spring暴露了一些很有用的 元数据。 
|表达式|描述|
|:---|:---|
|#root.args | 传递给缓存方法的参数，形式为数组 |
|#root.caches | 该方法执行时所对应的缓存，形式为数组 | 
|#root.target | 目标对象 | 
|#root.targetClass | 目标对象的类，是#root.target.class的简写形式 | 
|#root.method | 缓存方法 | 
|#root.methodName | 缓存方法的名字，是#root.method.name的简写形式 | 
|#result | 方法调用的返回值（不能用在@Cacheable注解上） | 
|#Argument | 任意的方法参数名（如#argName）或参数索引（如#a0或#p0） | 

**`unless`属性和`condition`属性的异同**： 
> unless和condition属性一点细微的差别。  
> unless属性**只能阻止将对象放进缓存**，但是在这个方法调用的时候，依然会去缓存中进行查找，如果找到了匹 的值，就会返回找到的值。  
> 与之不同，如果condition的表达式 计算结果为false，那么在这个方法调用的过程中，缓存是被禁用的。   
> 就是说，不会去缓存进行查找，同时返回值也不会放进缓存中。  

**`unless`属性和`condition`属性与`#result`**：  
> unless属性的表达式能够通过#result引用返回值。  
> 这是很有用的，这么做之所以可行是因为unless属性只有在缓存方法有返回值时才开始发挥作用。  
> 而condition肩负着在方法上禁用缓存的任务，因此它不能等到方法返回时再确定是否该关闭缓存。  
> 这意味着它的表达式必须要在进入方法时进行计算，所以我们不能通过 #result引用返回值。  

**缓存方法返回值与缓存注解**：  
> 与@Cacheable和@CachePut不同，@CacheEvict能够应用在返回值为void的方法上，  
> 而@Cacheable和@CachePut需要非void的返回值，它将会作为放在缓存中的 条目。   

### 使用XML声明缓存  

**使用XML声明缓存的原因**：  
- 你可能会觉得在自己的源码中添加Spring的注解有点不太舒服。  
- 你需要在**没有源码的bean**上应用缓存功能。  

**Spring的`cache`和`aopo命名空间**：  
> Spring的cache命名空间提供了使用XML声明缓存规则的方法，可以作为面向注解缓存的替代方案。  
> 因为缓存是一种 面向切面的行为，所以cache命名空间会与Spring的aop命名空间结合起来使用，用来声明缓存所应用的切点在哪里。  
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:cache="http://www.springframework.org/schema/aop"
    xmlns:aop="http://www.springframework.org/schema/cache"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
                        http://www.springframework.org/schema/beans/spring-beans.xsd
                        http://www.springframework.org/schema/aop 
                        http://www.springframework.org/schema/aop/spring-aop.xsd
                        http://www.springframework.org/schema/cache 
                        http://www.springframework.org/schema/cache/spring-cache.xsd">
                        
</beans>           
```

**Spring的`cache`命名空间元素**：  
```xml
<beans>
    <cache:annotation-driven/>  <!-- 启用注解驱动的缓存。等同于Java配置中的@EnableCaching -->
    <cache:advice id="用于aop通知器上的id"><!-- 定义缓存通知（advice）。结合<aop:advisor>，将通知应用 到切点上  -->
        <cache:caching cache="缓存名，定义此属性后可省略在子元素定义属性"><!-- 在缓存通知中，定义一组特定的缓存规则 -->
            <cache:cacheable
                cache="缓存名"
                method="缓存方法名"
                key="SpEL表达式，用来得到缓存的key（默认为方法的参数）"
                condition="SpEL表达式，如果计算得到的值为false，将会为这 个方法禁用缓存"
                unless="可以为这个可选的属性指定一个SpEL表达式，如果 这个表达式的计算结果为true，那么将会阻止将返回值放到缓存之 中"
            /><!-- 指明某个方法要进行缓存。等同于@Cacheable注解 -->
            <cache:cache-put
                cache="缓存名"
                method="缓存方法名"
                key="SpEL表达式，用来得到缓存的key（默认为方法的参数）"
                condition="SpEL表达式，如果计算得到的值为false，将会为这 个方法禁用缓存"
                unless="可以为这个可选的属性指定一个SpEL表达式，如果 这个表达式的计算结果为true，那么将会阻止将返回值放到缓存之中"
            /><!-- 指明某个方法要填充缓存，但不会考虑缓存中是否已有匹配 的值。等同于@CachePut注解 -->
            <cache:cache-evict
                cache="缓存名"
                method="缓存方法名"
                key="SpEL表达式，用来得到缓存的key（默认为方法的参数）"
                condition="SpEL表达式，如果计算得到的值为false，将会为这 个方法禁用缓存"
                all-entries="如果是true的话，缓存中所有的条目都会被移 除掉。如果是false的话，只有匹配key的条目才会被移除掉。"
                before-invocation="如果是true的话，缓存条目将会在方 法调用之前被移除掉。如果是false的话，方法调用之后才会移 除缓存。"
            /><!-- 指明某个方法要从缓存中移除一个或多个条目，等同于 @CacheEvict注解 -->
        </cache:caching>
    </cache:advice>
</beans>
```

**示例**：  
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:cache="http://www.springframework.org/schema/aop"
    xmlns:aop="http://www.springframework.org/schema/cache"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
                        http://www.springframework.org/schema/beans/spring-beans.xsd
                        http://www.springframework.org/schema/aop 
                        http://www.springframework.org/schema/aop/spring-aop.xsd
                        http://www.springframework.org/schema/cache 
                        http://www.springframework.org/schema/cache/spring-cache.xsd">
           
    <aop:cofing>
        <!-- 将缓存通知绑定到一个切点上，建立一个完整的切面 -->
        <aop:advisor advice-ref="cacheAdvice"
            pointcut="execution(* package.SpitterRepository.*(..)"/>
    </aop:cofing>    
    
    <cache:advice id="cacheAdvice">
        <cache:caching>
            <cache:cacheable
                cache="spittleCache"
                method="findRecent"/>
            <cache:cacheable 
                cache="spittleCache"
                method="findOne"/>
            <cache:cache-put
                cache="spittleCache"
                method="save"
                key="#result.id"/>
            <cache:cache-evict
                cache="spittleCache"
                method="remove"/>
        </cache:caching>
    </cache:advice>      
    
    <bean id="cacheManager" 
    class="org.springframework.cache.concurrent.ConcurrentMapCacheManager"/>   
</beans>   
```