## 保护方法应用  

**Web安全缺陷**： 
> Web安全是非常重要的，它能阻止用户访问没有权限的内容。  
> 但是一旦攻破Web安全，用户就可能请求不允许访问的内容。  

**保护方法**：  
> 我们可以同时保护应用的Web层以及场景后面的方法，这样就能保证 如果用户不具备权限的话，就无法执行相应的逻辑。  
> 通过声明安全规则，使用Spring Security保护bean方法，就能锁定方法，阻止无权限用户访问。  
> 其中的`@Secured`就相当于Servlet安全中自定义编程安全的用authenticate包裹的方法。  

**注意**：  
> 保护方法注解的方法所属于的类以及该方法安全配置类必须是属于Spring Bean , 即被Root.config类扫描加载。  
> Web安全配置了属于Web, 被Web.config扫描加载。  


### 使用注解保护方法  

**Spring Security提供了三种不同的安全注解**：   
> @Secured和@RolesAllowed方案非常类似，能够基于用户所授予 的权限限制对方法的访问。   
> @PreAuthorize和@PostAuthorize在方法上定义更灵活的安全规则。  
> @PreFilter和@PostFilter过滤返回以及传入方法的集合。 
- `@Secured`: Spring Security自带的@Secured注解； 
- `@RolesAllowed`: JSR-250的@RolesAllowed注解； 
- 表达式驱动的注解： 
    - `@PreAuthorize`  
    - `@PostAuthorize`  
    - `@PreFilter`  
    - `@PostFilter`   

**配置方法安全**：  
- `GlobalMethodSecurityConfiguration`配置类：配置方法安全的配置类需要继承该类。  
    - `void configure(AuthenticationManagerBuilder)`：可覆盖该方法来提供查询用户详细信息服务。 
    > 与`WebSecurityConfigurerAdapter`的`configure`方法等效。 
- `@EnableGlobalMethodSecurity`注解：开启相关的方法安全功能。 
    - `securedEnabled`: 开启`@Secured`注解功能。 
    - `jsr250Enabled`: 开启`@RolesAllowed`注解功能。
    
> `@Secured`和`@RolesAllowed`可以同时开启，并不冲突。  
> 由一个共同的不足是：  
> 它们只 能根据用户有没有授予特定的权限来限制方法的调用。  
> 在判断方式是 否执行方面，无法使用其他的因素。  

```java
@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true//。如果securedEnabled属 性的值为true的话，将会创建一个切点，这样的话Spring Security切 面就会包装带有@Secured注解的方法。
        ,jsr250Enabled = true  //true 则开启`@RolesAllowed`注解。
)
public class ExampleMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    //Web安全的配置类扩展了 WebSecurityConfigurerAdapter，与之类似，这个类能够为方 法级别的安全性提供更精细的配置。
    //例如，如果我们在Web层的安全配置中设置认证，那么可以通过重 载GlobalMethodSecurityConfiguration的configure()方 法实现该功能

}
//Spring MVC 中需要让RootConfig加载`GlobalMethodSecurityConfiguration`Bean以来配置方法安全细节。 
@Configuration
@ComponentScan(basePackageClasses = {RootConfig.class, ExampleMethodSecurityConfig.class},
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ANNOTATION, value= EnableWebMvc.class)
        })
public class RootConfig {}
```

**被保护方法的bean示例**:  
> 须得作为Spring Bean而非Web Bean被扫描创建才能是方法安全注解有效。  
```java

@Component
public class ExampleMethod {

    //接受String数组，认证用户必须至少具备其中的一个权限才能进行方法调用。
    //否则，抛出Spring Security异常：`AuthenticationException`或`AccessDeniedException`的子列。
    //它们是非检查型异常，但这个异常最终必须要被捕获和处 理。
    //如果被保护的方法是在Web请求中调用的，这个异常会被Spring Security的过滤器自动处理。
    //否则的话，你需要编写代码来处理这个 异常。
    @Secured({"ROLE_Admin"})
    public void useSecured(){
        System.out.println("@Secured");
    }

    @RolesAllowed({"ROLE_Admin"})
    public void useRolesAllowed(){
        System.out.println("@RolesAllowed");
    }
}
```

### 使用表达式实现方法级别的安全性   
**`@Secured`和`@RolesAllowed`的不足**： 
> 尽管@Secured和@RolesAllowed注解在拒绝未认证用户方面表现 不错，但这也是它们所能做到的所有事情了。  
> 有时候，安全性约束不 仅仅涉及用户是否有权限。  

**使用SpEL做参数的方法安全注解**： 
>  Spring Security 3.0引入了几个新注解，它们使用SpEL能够在方法调用 上实现更有意思的安全性约束。  

|注解|描述|
|:---|:---|
|`@PreAuthorize`|在方法调用之前，基于表达式的计算结果来限制对方法的访问。|
|`@PostAuthorize`|允许方法调用，但是如果表达式计算结果为false，将抛出一个安 全性异常。|
|`@PostFilter`|允许方法调用，但必须按照表达式来过滤方法的结果。|
|`@Prefilter`|允许方法调用，但必须在进入方法之前过滤输入值。|  

**`@EnableGlobalMethodSecurity`的`prePostEnable`属性启动上述四个注解**：  
> 们需要 将@EnableGlobalMethod-Security注解的prePostEnabled 属性设置为true，从而启用它们。  
```java
@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true//如果securedEnabled属 性的值为true的话，将会创建一个切点，这样的话Spring Security切 面就会包装带有@Secured注解的方法。
        ,jsr250Enabled = true  //true 则开启`@RolesAllowed`注解。
        ,prePostEnabled = true
)
public class ExampleMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    //Web安全的配置类扩展了 WebSecurityConfigurerAdapter，与之类似，这个类能够为方 法级别的安全性提供更精细的配置。
    //例如，如果我们在Web层的安全配置中设置认证，那么可以通过重 载GlobalMethodSecurityConfiguration的configure()方 法实现该功能
}
```

#### 表述方法访问规则  
> @PreAuthorize的表达式会在方法调用之前执行，如 果表达式的计算结果不为true的话，将会阻止方法执行。  
> 与之相 反，@PostAuthorize的表达式直到方法返回才会执行，然后决定 是否抛出安全性的异常。   

**`@PreAuthorize`和`@PostAuthorize`示例**：  
- 内置表达式`returnObject`: 使用@PostAuthorize时我们可以使用内置的表达式returnObject表示方法的返回值。  
```java
@Component
public class ExampleMethod {

    @PreAuthorize(
            "(hasRole(\"ROLE_Manager\")" +//在方法前验证权限
            "and #count > 0) " +
            "or hasRole(\"ROLE_Admin\")"//增加更详细的安全规则。
    )
    //使用@PostAuthorize时我们可以使用内置的表达式returnObject表示方法的返回值。
    @PostAuthorize(
            "returnObject < 100"//时候验证返回结果，失败则排除异常。
    )
    public int useAuthorize(int count){
        System.out.println("@PreAuthorize");
        return count * 10;
    }
}
```

**`principal`** 
> 是另一个Spring Security内置的 特殊名称，它代表了当前认证用户的主要信息（通常是用户名）。  
```java
public class ExampleMethod{
    @PostAuthorize("returnObject.spitter.username == principal.username")
    public Spitter getSpittleById(long id){}
}
```

**注意使用`@PostAuthorized`**:  
> 有一点需要注意，不像@PreAuthorize注解所标注的方法那 样，@PostAuthorize注解的方法会首先执行然后被拦截。  
> 这意味 着，你需要小心以保证如果验证失败的话不会有一些负面的结果。  

#### 过滤方法的输入和输出   
> 有时，需要保护的并不是对方法的调用，需要保护的是传 入方法的数据和方法返回的数据。  

**`@PostFilter`和`@PreFilter`**：  
> @PostFilter会使用这个表达式计算该方法所返回集合的每个 成员，将计算结果为false的成员移除掉。  
> @PreFilter也使用SpEL来过滤集合， 只有满足SpEL表达式的元素才会留在集合中。  
- `@PreFilter`的`filterTarget`属性：   
    > 当@PreFilter标注的方法拥有多个集合类型的参数时，需要通过@PreFilter的filterTarget属性指定当前@PreFilter是针对哪个参数进行过滤的。  
- 内置表达式`filterObject`：  
    > filterObject是使用@PreFilter和@PostFilter时的一个内置表达式，表示集合中的当前对象。  

```java
@Component
public class ExampleMethod {
    @PreAuthorize("hasAnyRole({\"ROLE_Admin\", \"ROLE_Manager\"})")
    //filterObject是使用@PreFilter和@PostFilter时的一个内置表达式，表示集合中的当前对象。
    //当@PreFilter标注的方法拥有多个集合类型的参数时，需要通过@PreFilter的filterTarget属性指定当前@PreFilter是针对哪个参数进行过滤的。
    @PreFilter(//将输入集合计算结果未false的成员移除。
            filterTarget = "list",
            value = "hasRole(\"ROLE_Admin\") || " +
                    "filterObject > 0"
    )
    @PostFilter(//将输出集合计算结果未false的成员移除。
            "hasRole(\"ROLE_Admin\") || " +
                    "filterObject < 100"
    )
    public List<Integer> useFilter(List<Integer> list){
        return list;
    }
}

```

##### 许可计算器  

**安全表达式难以测试和调试**： 
> 使用表达式来定义安全限制是一种更为强大的方式， 即便如此，我们也不应该让表达式过于聪明智能。  
> 我们应该避免编写 非常复杂的安全表达式，或者在表达式中嵌入太多与安全无关的业务 逻辑。  
> 而且，表达式最终只是一个设置给注解的String值，因此它很难测试和调试。  

**许可计算器**： 
> 如果你觉得自己的安全表达式难以控制了，那么就应该看一下如何编 写自定义的许可计算器（permission evaluator），以简化你的SpEL表 达式。  

**`hasPermission()`表达式**：  
> hasPermission()函数是Spring Security为SpEL提供的扩展，  
> 它为 开发者提供了一个时机，能够在执行计算的时候插入任意的逻辑。   
> 我 们所需要做的就是编写并注册一个自定义的许可计算器。  

**默认许可计算器`DefaultMethodSecurityExpressionHandler`使用`DenyAllPermissionEvaluator`**：  
> 默认情况下，Spring Security会配置为使 用DefaultMethodSecurityExpression-Handler， 
> 它会使用 一个DenyAllPermissionEvaluator实例。 
> 顾名思义，Deny- AllPermissionEvaluator将会在hasPermission()方法中始 终返回false，拒绝所有的方法访问。  

**配置步骤**： 
1. 继承`PermissionEvaluator`定义许可计算器。 
2. **重载`GlobalMethodSecurityConfiguration`的`createExpressionHandler()`**方法许可计算器注册到Spring Security中。  
> 为Spring Security提供另外一个DefaultMethodSecurityExpressionHandler，让它使用我们自定义的 SpittlePermissionEvaluator，  
> 这需要重 载GlobalMethodSecurityConfiguration的 createExpressionHandler方法。  

**示例**： 
```java
//定义许可计算器 
public class ExamplePermissionEvaluator implements PermissionEvaluator {
   private static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority("ROLE_Admin");

   @Override//将要评估的对象作为第二个参数
   public boolean hasPermission(Authentication authentication, Object target, Object permission) {

       if(target instanceof  Integer){
           if("delete".equals(permission)){
               return authentication.getAuthorities().contains(ADMIN_AUTHORITY)
                       || (Integer)target > 0;
           }
       }

       throw new UnsupportedOperationException(
               "hasPermission not supported for object <" + target + ">" +
                       "and permission < " + permission + ">");
   }

   @Override//目标对象ID可以得到时候才有用，并将ID作为Serializable传入第二个参数。
   public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
       throw new UnsupportedOperationException();
   }
}
//注册许可计算
@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true//如果securedEnabled属 性的值为true的话，将会创建一个切点，这样的话Spring Security切 面就会包装带有@Secured注解的方法。
        ,jsr250Enabled = true  //true 则开启`@RolesAllowed`注解。
        ,prePostEnabled = true
)
public class ExampleMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    //注册许可计算器  
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler(){
        DefaultMethodSecurityExpressionHandler expressionHandler = 
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new ExamplePermissionEvaluator());
        return expressionHandler;
    }
}

```