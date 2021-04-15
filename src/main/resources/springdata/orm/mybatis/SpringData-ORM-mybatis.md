## ORM Mybatis  

`@Repository`: 
> `@Repository`能在Repository的类上使用不依赖Spring的原生ORM框架的session时，将框架特定的异常转化为Spring数据库异常体系。   
> 这样子就不需要在持久化实现中依赖Spring（的模板类）。  
> 它实际有两个作用： 
- 构造性作用：相当于`@Component`，是被注解的类能作为Bean被Spring的自动扫描机制发现。  
- 转化ORM异常：它有一项任务就是捕获平台相关的异常，然后 使用Spring统一非检查型异常的形式重新抛出。  

**`PersistenceExceptionTranslationPostProcessor`bean启动`@Repostory`转化异常的功能**：  
> 为了给不使用模板的Repository添加异常转换功能，我们只 需在Spring应用上下文中添加一 个`PersistenceExceptionTranslationPostProcessor` bean。   
> `PersistenceExceptionTranslationPostProcessor`是一个 bean 后置处理器（bean post-processor），它会在所有拥 有@Repository注解的类上添加一个通知器（advisor），  
> 这样就会 捕获任何平台相关的异常并以Spring非检查型数据访问异常的形式重新抛出。  
```java
@Configuration 
public class ExampleConfiguration{
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
```

`@Transactional`注解事务:  
- `@Transactional`表明这个`Repository`中的持久化方法是在事务上下文中执行的。  

#### Spring 事务  
[https://www.cnblogs.com/xusir/p/3650522.html](https://www.cnblogs.com/xusir/p/3650522.html)  
[https://blog.csdn.net/qq_27986857/article/details/79972411](https://blog.csdn.net/qq_27986857/article/details/79972411)  
[https://blog.csdn.net/qq_36544760/article/details/82667864](https://blog.csdn.net/qq_36544760/article/details/82667864)  

### 
