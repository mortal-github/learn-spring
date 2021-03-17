## Advance Wiring  

### 环境与Profile  
- 环境差异：软件开发，和软件部署的时候，环境配置是有差异。  
- 重构风险：环境配置差异（开发，QA，部署）将导致常常需要重构软件，这可能会会带来风险（带来Bug,不同环境下Bug来源可能不同，环境切换可能带来一些bug）。  
- `Bean Profile`解决方案：Spring提供了`Bean Profile`解决方法，它**不需要重构**，它在**运行时**而不是构建时确定创建那个Bean。  
>Spring为环境相关的bean所提供的解决方案其实与构建时的方案没有 太大的差别。  
>当然，在这个过程中需要根据环境决定该创建哪个bean 和不创建哪个bean。  
>不过Spring并不是在构建的时候做出这样的决 策，而是等到运行时再来确定。  
>这样的结果就是同一个部署单元（可 能会是WAR文件）能够适用于所有的环境，没有必要进行重新构建。  

`bean profile`功能： 
- 引入`Bean profile`功能： 在3.1版本中，Spring引入了bean profile的功能。要使用profile，  
- **将`Bean`整理到`profile`中**： 你首 先要将所有不同的bean定义整理到一个或多个profile之中，  
- **激活`profile`**: 在将应用 部署到每个环境时，要确保对应的profile处于激活（active）的状态。  

#### Java中配置profile  

`@Profile`注解：  
- **`@Profile`**注解**指定Bean归属的profile** ：在**配置类中**，可以使用@Profile注解指定某个bean属于哪一个 profile。  
- **注解位置**： Spring3.1版本中，只能在类级别上使用；Spring3.2开始可以在方法级别上使用。   
- **激活相应的Bean**: 
    - 没有指定profile的Bean: 没有指定profile的bean始终都会被创建，
    - 指定profile的Bean: 只有规定的**profile激活时，相应的bean才会被创建**，但是可能会有其他的bean并没有声明在一个给定的profile范围 内。
```java
@Configuration 
@Profile("dev")//在类级别上使用
class DevelopmentProfileConfig {
    @Bean(destroyMethod="shutdown")
    public DataSource dataSource(){
    }
}

@Configuration 
class DataSourceConfig{
    @Bean(destroyMethod="shutdown")
    @Profile("dev")         //为dev profile装配的bean。
    public DataSource embeddedDataSource(){
    }
    
    @Bean
    @Profile("prod")        //为prod profile装配的bean。
    public DataSource jndiDataSource(){
    }
}
```

#### 在XML中配置profile 
- `<beans>`根元素的`profile`属性： 在根元素上指定profile属性即配置了**这个XML配置文件所属的profile**。  
- 使用嵌套的`<beans>`子元素上的`profile`属性： 可以在**同一个配置文件**上通过嵌套多个并列的`<beans>`来**指定多个profile**所包含的Bean。  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
       http://www.springframework.org/schema/beans
      http://www.springframework.org/schema/beans/spring-beans.xsd"
        profile="dev"><!--指定profile-->
</beans>
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
       http://www.springframework.org/schema/beans
      http://www.springframework.org/schema/beans/spring-beans.xsd">
        <!--dev profile-->
        <beans profile="dev">
        </beans>
        
        <!--QA profile-->
        <beans profile="qa">
        </beans>

        <!--prod profile-->
        <beans profile="prod">
        </beans>
</beans>
``` 

#### 激活profile  
- **`spring.profiles.active`**属性：指定**激活的profile**。  
- **`spring.profiles.default`**属性: 指定**默认激活**的profile。
>如果设置了`spring.profiles.active`，则激活这个属性所指的profile。  
>否则，如果设置了`spring.profiles.default`，则激活默认属性所指的profile。   
>否则，则没有激活任何profile。  

**设置属性的方法**： 
- 作为**`DispatcherServlet`**的**初始化参数**。  
- 作为Web应用的**上下文参数**。   
- 作为JNDI条目； 
- 作为环境变量； 
- 作为JVM的系统属性； 
- 在集成测试类上，使用**`@ActiveProfiles`**注解设置。  

### 条件化装配`@Conditional`  
- **`@Conditional`**注解： Spring4引入了`@Conditional`注解，与`Bean`或`Component`注解配合，条件化创建Bean。  
- **`Condition`**接口： `@Conditional`注解通过**`Condition`**参数的**`matches`**方法判断是否创建Bean。  
```java
@Configuration 
class Example{
    @Bean
    @Conditional(MagicExistsCondition.class)//通过Condition接口判断
    public MagicBean magicBean(){
        return new MagicBean();
    }   
}
//实现Condition接口创建判断逻辑
class MagicExistsCondition implements Condition{
    public boolean matches(ConditionContext context, AnnnotatedTypeMetadata metadata){
       Environment env = context.getEnvironment();
        return env.containsProperty("magic");
    }
}
```

`Condition`接口：  
```java
public interface Condition{
    boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
}
```

`ConditionContext`接口： 
```java
public interface ConditionContext{
    BeanDefinitionRegistry getRegistry();                   //检查Bean定义
    ConfiguratableListableBeanFactory getBeanFactory();     //检查Bean是否存在甚至探查Ben的属性
    Environment getEnvironment();                           //检查环境变量
    ResourceLoader getResourceLoader();                     //读取并探查所加载资源
    ClassLoader getClassLoader();                           //加载并检查了是否存在。
}
```

`AnnotatedTypeMetadata`接口：
AnnotatedTypeMetadata则能够让我们**检查带有@Bean**注解的方法上还有什么**其他的注解**。  
```java
public interface AnnotatedTypeMetadata{
    boolean isAnnotated(Sttring annotationType);        //判断是否有其他特定注解。
    Map<String, Object> getAnnotationAttributes(String annotationType);
    Map<String, Object> getAnnotationAttributes(String annotationType, boolean classValuesAsString);
    MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationType);
    MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationType, boolean classValueAsString);
}
```

Spring4的`@Profile`重构为使用了`@Conditional`,`Condition`：
```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ProfileCondition.class)
public @interface Profile{
    String[] value();
}
class ProfileCondition implements Condition{
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata){
           if(context.getEnvironment() != null){
               MultiValueMap<String, Object> attrs = 
                    metadata.getAllAnnotationAttributes(Profile.class.getName());
                if(attrs != null){
                    for(Object value : attrs.get("value")){
                        if(context.getEnvironment().acceptsProfiles(((String[]) value))){
                            return true;
                        }
                    }
                    return false;
                }
           }
            return true;
    }
}
```

### 解决自动化装配歧义`@Primary,@Qualifier`  
- `NoUniqueBeanDefinitionException`异常：当有多个Bean发生歧义时的时候，就会抛出该异常。  
-  `@Primary`注解和`primary`属性：可以设置首选bean,发生歧义的时候则使用首选Bean(若有多个首选依旧冲突)。  
- `@Qualifier`注解：限定符，通过限定符以及限定符注解组合，缩小Bean搜索范围知道一个Bean，从而解决歧义。  

#### 标示首选Bean 
- **`@Primary`**注解与**`@Component`**注解配合表示首选Bean。  
- **`@Primary`**注解与**`@Bean`**注解配合表示首选Bean。  
- **`<bean>`**元素的**`primary`**属性指定**是否**为首选Bean。  
```java
@Component
@Primary
public class IceCream implements Dessert{}
```
```java
@Configuration 
public class Example{
    @Bean
    @Primary
    public Dessert iceCream(){
    }
}
```
```xml
<bean id="iceCream" class="package.IceCream"
    primary="true"> 
</bean>
```

#### 限定自动装配的Bean 
- **`@Qualifier`**注解与`@Autowired`注解配合： 在注入时通过**限定符**限制范围来指定注入的Bean。  
- Bean的**默认限定符**：Bean的默认限定符**与ID相同**。  
- **`@Qualifier`**注解与**`@Component`或`@Bean`**配合用其**`value`**属性（Spring类型）**自定义**Bean的限定符。   
- **限定符注解**：使用`@Qualifier`**构建注解**，再用这些注解**代替`@Qualifier`**，**即类型安全，又能组合限定符**。（不允许同时使用多个`@Qualifer`）  
>基于默认的bean ID作为限定符是非常简单的，但这有可能会引入一些 问题。例如重构Bean的类名。  
>用自定义的@Qualifier值时，最佳实践是为bean选择特征性或 描述性的术语，  
>面向特性的限定符要比基于bean ID的限定符更好一些。但是，如果多个bean都具备相同特性的话，这种做法也会出现问题。  
>再次遇到了歧义性的问题，需要使用更多的 限定符来将可选范围限定到只有一个bean。
```java
public class ExampleAutowired{
    @Autowired 
    @Qualifier("ircCream")
    public void setDessert(Dessert dessert){
    }
}
```
```java
@Component 
@Qualifier("cold")
public class ExampleComponent{

}
```
```java
@Configuration
public class ExampleBean{
    @Bean
    @Qualifier("cold")
    public Dessert iceCream(){}
}
```

```java
//用来组合的限定符注解1
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Rentention(RententionPolicy.RUNTIME)
@Qualifier
public @interface Cold{}
```
```java
//用来组合的限定符注解2
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Rentention(RententionPolicy.RUNTIME)
@Qualifier
public @interface Creamy{}
```
```java
//在Bean上组合限定符
@Component
@Cold
@Creamy
class IceCream implements Dessert{}
```
```java
public class ExampleQualifierAnnotation{
//在注入点通过组合限定符选择Bean
@Autowired
@Cold
@Creamy
public void setDessert(Dessert dessert){}
}
```

### Bean作用域  
- **默认单例**作用域：默认请况下，Spring应用上下文中所有Bean都是单例。  
- Spring定义了**多种作用域**：可以基于这些作用域创建Bean。
    - 单例（Singleton)：在整个应用程序，**只创建Bean的一个实例**。 
    - 原型（Prototype)：每次注入或通过Spring引用上下文获取的时候，**都会创建一个新**的Bean实例。  
    - 会话（Session)：在**Web**应用中，为**每个会话**创建一个Bean实例。  
    - 请求（Request)：在**Web**引用中，为**每个请求**创建一个Bean实例。  
    
#### 指定其他作用域：
- 单例时默认的作用域： 
- Java中使用**`@Scope`**注解和**`Component`或`Bean`**注解用其**value**值（String类型）配合：指定其他作用域。
    - `ConfigurableBeanFatory。SCOPE_PROTOTYPE`：指定为原型域，使用常量更安全。 
- XML配置中使用**`<bean>`**的**`scope`**属性：`prototype`值指定为原型域。 
```java
@Component 
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Notepad{}
```
```java
@Configuration
public class BeanConfig{
    @Bean
    @Scope("prototype")
    public Notepad notepad(){
        return new Notepad();
    }   
}
```
```xml
<bean id="notepad" class="package.notepad"
    scope="prototype"/>
```

#### 会话和请求作用域： 
- 作用域代理：**`@Scope`**属性有一个**`proxyMode`**属性，性解决了**将会话或请求作用域的bean注入到单例bean中**所遇到的问题。 
    - 接口代理：如果Bean类型是接口，则`proxyMode`要用**`ScopedProxyMode.INTERFACES`**值，以表示代理实现Bean类型的接口。  
    - 类代理： 如果Bean类型是类，则`proxyMode`要用**`ScopedProxyMode.TARGET_CLASS`**值。
- 会话请求作用域： 
        - `WebApplicationContext.SCOPE_SESSION`：会话作用域。
>如果Bean类型是类, 则Spring就没有办法创建基于接口的代理了。此时，它必须使用CGLib 来生成基于类的代理。
```java
//会话作用域Bean
@Component
@Scope(
    value=WebApplicationContext.SCOPE_SESSION,
    proxyMode=ScopedProxymode.INTERFACES
)
public interface ShoppingCart{}
```
```java
//会话作用域bean注入到单例作用域bean
@Component 
public class StoreService{
    @Autowired
    public void setShoppingCart(ShoopingCart shoppingCart){}
}
```
>因为StoreService是一个单例的bean，会在Spring应用上下文加载 的时候创建。  
>当它创建的时候，Spring会试图将ShoppingCart bean 注入到setShoppingCart()方法中。  
>但是ShoppingCart bean是 会话作用域的，此时并不存在。  
>直到某个用户进入系统，创建了会话 之后，才会出现ShoppingCart实例。  
>另外，系统中将会有多个ShoppingCart实例：每个用户一个。我 们并不想让Spring注入某个固定的ShoppingCart实例 到StoreService中。  
>我们希望的是当StoreService处理购物车 功能时，它所使用的ShoppingCart实例恰好是当前会话所对应的那一个。  
>Spring并不会将实际的ShoppingCart bean注入到StoreService中， Spring会注入一个到ShoppingCart bean的代理，  
>如图3.1所示。这 个代理会暴露与ShoppingCart相同的方法，所以StoreService 会认为它就是一个购物车。  
>但是，当StoreService调 用ShoppingCart的方法时，代理会对其进行懒解析并将调用委托 给会话作用域内真正的ShoppingCart bean。

#### 在XML中声明作用域代理  
- 在XML要设置代理模式，我们需要使用Spring aop命名空间的一个新元素。  
- **`<aop:scoped-proxy>`**是与@Scope注解的proxyMode属性功能相 同的Spring XML配置元素。
    - 它会告诉Spring为bean创建一个作用域代理。
    - **默认代理目标类**：默认情况下，它会使用CGLib创建**目标类的代理**。
    - **指定代理接口**：但是我们也可 以将**`proxy-target-class`属性设置为`false`**，进而要求它生成基于接口的代理。
- 为了使用`<aop:scoped-proxy>`元素，我们必须在XML配置中声明 Spring的aop命名空间
```xml
<!--目标类代理-->
<bean id="cart" class="package.ShoppingCart"
    scope="session">
    <aop:scoped-proxy/>
</bean>
```  
```xml
<!--基于接口的代理-->
<bean id="cart" class="package.ShoppingCart"
    scope="session">
    <aop:scoped-proxy proxy-target-class="false"/>
</bean>
```     

### 运行时值注入  
>有时候硬编码是可以的，但有的时候，我们可能会希望避免硬编码值，而是想让这些值在运行时再确定。

Spring提供了两种在运行时求值的方式： 
- 属性占位符（Property placeholder）。   
- Spring表达式语言（SpEL）。  

#### 注入外部的值  
使用**`@PropertySource`**注解和**`Environment`**：处理外部值的最简单方式就是**声明属性源**并通过`Environment`来**检索属性**。  
>@PropertySource中指定的文件的属性将会被加载到Environment中。  
>接下来就可以使用Environment获取属性。   
```java
@Configuration
@PropertySource("classpath:/META-INF/app.properties")
public class ExpressiveConfig{
    @Autowired
    Enviroment env;
    
    @Bean
    public BlankDisc disc(){
        env.getProperty("disc.title");
        env.getProperty("disc.artist");
    }
}
```

#### `Environment`  
```java
public interface Environment{
    //getProperty方法的四个重载方法
    String getProperty(String key);
    String getProperty(String key, String defaultValue);
    //不将所有值都视为String类型，可以获取指定类型的属性。 
    T getProperty(String key, Class<T> type);   
    T getProperty(String key, Class<T> type, T defaultValue);
    //希望属性必须定义，即获取到的值不能是null，否则抛出IllegalStateException异常。 
    String getRequiredProperty(String key);
    //检查属性是否存在 
    Boolean containsProperty();
    //将属性解析为类 
    Class<T> getPropertyAsClass(String key, Class<T> type);
    //检查profile 
    String[] getActiveProfiles(); //返回激活profile名称的数组。 
    String[] getDefaultProfiles(); //返回默认profile名称的数组。 
    boolean acceptsProfiles(String... profiles);    //判断是否支持给定profile 
}
```

#### 占位符装配属性  
> 直接从Environment中检索属性是非常方便的，尤其是在Java配置 中装配bean的时候。  
> 但是，Spring也提供了通过占位符装配属性的方 法，这些占位符的值会来源于一个属性源。

- 在外部**定义属性**：Spring一直支持将属性定义到**外部的属性的文件**中，并使用占位符值 将其插入到`Spring bean`中。  
- **`${...}`**占位符：在Spring装配中，占位符的形式为使用`${ ... }`包装的属性名称。  
- **`@Value`**注解： 在自动装配的Bean中，使用`@Value`注解来使用占位符。 
```java
@Component
public class BlankDisc{
    //...
    public BlankDisc(@Value("${disc.title}") String title, 
                     @Value("${disc.title}") String artist){
        //...
    }   
}
```

**启用占位符**：  
>实践发现似乎不需要特意启动使用占位符。  
- java配置： 为了使用占位符，我们必须要配置一个**`PropertyPlaceholderConfigurer`**bean 或`PropertySourcesPlaceholderConfigurer`bean。
        从Spring 3.1开始，推荐使 用`PropertySourcesPlaceholderConfigurer`，因为它能够基 于`Spring Environment`及其属性源来解析占位符
- XML配置: **Spring context命名空间**中的**`<context:property-placeholder>`**元素将会为你生成`PropertySourcesPlaceholderConfigurer`bean：
```java
@Configuration
public class ExampleConfig{
    @Bean 
    public static PropertySourcesPlaceholderConfigurer getPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http:/www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/spring-beans.xsd
        http://www.springframework.org/schema/context 
        http://www.springframework.org/schema/spring-context.xsd">
        <context:property-placeholder/>
</beans>
```

#### SpEL Spring表达式语言  
> Spring 3引入了Spring表达式语言（Spring Expression Language， SpEL），  
> 它能够以一种强大和简洁的方式将值装配到bean属性和构造 器参数中，在这个过程中所使用的表达式会在运行时计算得到值。  
> 使用SpEL，你可以实现超乎想象的装配效果，这是使用其他的装配技 术难以做到的（甚至是不可能的）。 

SpEL拥有很多特性，包括： 
- 使用bean的ID来引用bean； 
- 调用方法和访问对象的属性； 
- 对值进行算术、关系和逻辑运算； 正
- 则表达式匹配； 
- 集合操作。 

**`#{...}`**：
- **`#{...}`**：就是SpEL表达式要放到“#{ ... }”之中， 除去“#{ ... }”标记之后，剩下的就是SpEL表达式体了。  
- **`#{123}`**：表示数字常量。 
- **`#{T(System).currentTimeMillis()}`**：调用静态方法，T()表达式调用静态域，静态方法。。T()表达式 会将java.lang.System视为Java中对应的类型，因此可以调用 其static修饰的currentTimeMillis()方法。
- **`#{sgtPeppers.artist}`**： 以引用其他的bean或其他bean的属性。  
- **`#{systemProperties['disc.title']}`**： 通过systemProperties对象引用系统属性。  

- 表示字面值： 数字，String类型，true,false，科学计数法。  
- 引用Bean、属性和方法：`#{sgtPeppers}`,`#{sgtPeppers.artist}`,`#{artistSelector.selectArtist()}`。  
- `?.`解决类型安全：`{artistSelector.selectArtist()?.toUpperCase()}`这个运算符能够在访问它右边的内容之前，确保它所对应的元素不是null
- `T()`在表达式中使用类型：如果要在SpEL中访问类作用域的方法和常量的话，要依赖T()这个运算符。 
    > T()运算符的结果会是一个Class对象，  
    > 可以将其装配到一 个Class类型的bean属性中。  
    > 但是T()运算符的真正价值在于它能够 访问目标类型的静态方法和常量。  
- 算术运算：`+`、`-`、`*`、`/`、`%`、`^`。  
- 比较运算：`<、>、==、<=、>=、lt、gt、eq、le、ge`。  
- 逻辑运算：`and、or、not、|`。  
- 条件运算：`<条件>?<成功执行语句>:<失败执行语句> (ternary)`、`<对象>?:<对象为null时返回的默认值>(Elvis)`。  
- 正则表达式：`<文本> matches <表达式>`.   
- 计算集合： 
    - `[<索引>]`：来从集合或数组中按照索引获取元素，还 可以从String中获取一个字符。
    - `.?[<判断表达式>]`：**过滤出子集**，对集合进行过滤，得到集合的一个子集。  
    - `.^[<判断表达式>]`、`.$[<判断表达式>]`：在集合中查询第一个匹配项和最后一个匹配项。  
    - `.![<属性>]`：投影运算符，它会从集合的每个成员中**选择特定的属性放到另外一个集合中**。  
> 当使用String类型的值时，“+”运算符执行的是连接操作。   
> 比较运算符有两种形式：符号形式和文本形式。  
> 
> SpEL还提供了三元运算符（ternary），它与Java中的三元运算符非常类似。
> `#{scoredboard.score > 1000 ? "Winner!" : "Loser"}`  
> 
> 三元运算符的一个常见场景就是检查null值，并用一个默认值来替 代null。这种表达式通常称为Elvis运算符。  
> `#{disc.title?: 'Rattle and Hum'}`
> `#{admin.email matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com'}`  

```
#{jukebox.songs[4].title}
#{'This is a test'[3]}
#{jukebox.songs.?[artis eq 'Aerosmith']} //Aerosmith的所有歌曲
#{jukebox.songs.^[artist eq 'Aerosmith']} //列表中第一个artist属性为Aerosmith的歌曲
#{jukebox.songs.$[artist eq 'Aerosmith']} 列表中最后一个artist属性为Aerosmith的歌曲：
#{jukebox.songs.![title]} //投影title属性 
#{jukebox.songs.?[artist eq 'Aerosmith'].![title]}1
```