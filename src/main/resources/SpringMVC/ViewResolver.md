## 视图解析器  
> 将控制器中请求处理的逻辑和视图中的渲染实现解耦是Spring MVC的 一个重要特性。  
> 控制器方法和 视图的实现会在模型内容上达成一致，这是两者的最大关联，除此之 外，两者应该保持足够的距离。  

 这看起来非常简单。我们所需要做的
**接口API**:  
```java
package org.springframework.web.servlet.view;

//ViewResolver的任务时接受控制器返回逻辑视图名解析出实际视图。 
public interface ViewResolver{
    View resolveViewName(String viewName, Locale locale)throws Exception;
}

//View接口的任务就是接受模型以及Servlet的request和response对象， 并将输出结果渲染到response中。
public interface View{  
    String getContentType();
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response);
}
```

**Spring内置13个视图解析器**：
```java
package org.springframework.web.servlet.view;
//Spring3.1支持除Tiles3 TileViewResolver之外的所有视图解析器。 
//Spring4,Spring3.2支持以下所有视图解析器。  


//将视图解析为Spring应用上下文中的bean,其中bean的ID与视图的名字相同。  
public class BeanNameViewResolver implements ViewResolver{}
//通过考虑客户端需要的内容类型来解析视图，委托给另外一个能够产生对应内容类型的视图解析器。   
public class ContentNegotiatingViewResolver implements ViewResolver{}
//将视图接卸为FreeMarker模板。  
public class FreeMarkerViewResolver implements ViewResolver{}
//将视图解析为Web应用的内部资源（一般为JSP）。  
public class InternalResourceViewResolver implements ViewResolver{}
//将视图解析为JasperReports定义。  
public class JasperReportsViewResolver implements ViewResolver{}
//将视图解析为资源bundle（一般为属性文件）。  
public class ResourceBundleViewResolver implements ViewResolver{}
//将视图解析为Apache Tile定义， 其中title ID与视图名称相同（有两个实现分别对应Tiles2.0和Tiles3.0）。 
public class TilesViewResolver implements ViewResolver{}
//直接根据视图的名称解析视图，视图的名称会匹配一个物理视图的定义。  
public class UrlBasedViewResolver implements ViewResolver{}
//将视图解析为Velocity布局，从不同的Velocity模板中组合页面。  
public class VelocityLayoutViewResolver implements ViewResolver{}
//将视图解析为Velocity模板。  
public class VelocityViewResolver implements ViewResolver{}
//将视图解析为特定XML文件中的bean定义，类似与BeanNameViewResolver。  
public class XmlViewViewResolver implements ViewResolver{}
//将视图解析为XSLT转换后的结果。  
public class XsltViewResolver implements ViewResolver{}

```


### JSP视图  

**Spring提供两种支持JSP视图的方式**：  
- `InternalResourceViewResolver`: 将视图名解析为JSP文件。   
    - 使用JSTL标签：若JSP文件使用了JSTL标签，则解析为 JstlView形式的JSP文件，从而将JSTL本地化和资源bundle变量暴 露给JSTL的格式化（formatting）和信息（message）标签。  
    - 层次结构：当逻辑视图名中包含斜线时，这个斜线也会带到资源的路径名中，以次可以构建层级目录结果。  
- Spring提供两个JSP标签库：  
    - 一个用于**表单到模型**的绑定。  
    - 一个提供了通用的工具类特性。  
> 不管你使用JSTL，还是准备使用Spring的JSP标签库，配置解析JSP的视图解析器都是非常重要的。  
> 尽管Spring还有其他的几个视图解析器都能将视图名映射为JSP文件，但就这项任务来讲，InternalResourceViewResolver是最简单和最常用的视图 解析器。  

#### 配置适用于JSP的视图解析器
> 有一些视图解析器，如ResourceBundleViewResolver会直接将 逻辑视图名映射为特定的View接口实现， 
> InternalResourceViewResolver所采取的方式并不那么直 接。它遵循一种约定，会在视图名上添加前缀和后缀，进而确定一个 Web应用中视图资源的物理路径。  

```java
//Java配置
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {WebConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter{
    
    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver resolver = new IntegerResourceViweResolver();
        resolver.setPrefix("/WEB-INF/views");//因为一般都需要模型来渲染视图，故放置在/WEB-INF文件夹下，防止浏览器直接请求。  
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }
}
```
```xml
<!-- xml配置 -->
<beans>
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"
        p:prefix="/WEB-INF/view"
        p:suffix=".jsp"/>
</beans>
```

**`InteralResourceViewResolver`解析示例**：
> InternalResourceViewResolver配置就绪之后，它就会将逻辑 视图名解析为JSP文件。  
> 当逻辑视图名中包含斜线时，这个 斜线也会带到资源的路径名中。  
> 以此可以将视图模板组织为层级目录结构，而不是将它们都放到同一个目录之中。  
- home将会解析为“/WEB-INF/views/home.jsp” 
- productList将会解析为“/WEB-INF/views/productList.jsp” 
- books/detail将会解析为“/WEB-INF/views/books/detail.jsp” 

**解析JSTL视图**：  
> 如果想让`InternalResourceViewResolver`将视图解析为`JstlView`，而不是`InternalResourceView`的话，那么我们只需设置它的`viewClass`属性即可。   
```java
//Java配置
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {WebConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter{
    
    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver resolver = new IntegerResourceViweResolver();
        resolver.setPrefix("/WEB-INF/views");//因为一般都需要模型来渲染视图，故放置在/WEB-INF文件夹下，防止浏览器直接请求。  
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }
}
```
```xml
<!-- xml配置 -->
<beans>
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"
        p:prefix="/WEB-INF/view"
        p:suffix=".jsp"
        p:viewClass="org.springframework.web.servlet.view.JstlView"/>
</beans>
```

**JstlView补充说明**：  
> 如果这些JSP使用JSTL标签来处理格式化和信息的话，那么我们会希 望InternalResourceViewResolver将视图解析为JstlView。  
> JSTL的格式化标签需要一个Locale对象，以便于恰当地格式化地域 相关的值，如日期和货币。  
> 信息标签可以借助Spring的信息资源和 Locale，从而选择适当的信息渲染到HTML之中。  
> 通过解析 JstlView，JSTL能够获得Locale对象以及Spring中配置的信息资源。 


#### 使用Spring的JSP标签库  

##### 将表单绑定到模型上
> Spring的表单绑定JSP标签库包含了14个标签，它们中的大多数都用来渲染HTML中的表单标签。  
> 但是，它们与原生HTML标签的**区别在于它们会绑定模型中的一个对象**，能够根据模型中对象的属性**填充值**。   
> 标签库中还包含了一个**为用户展现错误的标签**，它会将错误信息渲染 到最终的HTML之中。  

**标签库**：  
```jsp
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<sf:errors>         <%在一个HTML <span>中渲染输入域的错误%>
<sf:form>           <%渲染成一个HTML <form>标签，并为其内部标签暴露绑定路 径，用于数据绑定%>
<sf:label>          <%渲染成一个HTML <label>标签%>

<sf:input>          <%渲染成一个HTML <input>标签，其中type属性设置为text%>
<sf:hidden>         <%渲染成一个HTML <input>标签，其中type属性设置为hidden%>
<sf:password>       <%渲染成一个HTML <input>标签，其中type属性设置 为password%>

<sf:radiobutton>    <%渲染成一个HTML <input>标签，其中type属性设置为radio%>
<sf:radiobuttons>   <%渲染成多个HTML <input>标签，其中type属性设置为radi%>

<sf:checkbox>       <%渲染成一个HTML <input>标签，其中type属性设置 为checkbox%>
<sf:checkboxes>     <%渲染成多个HTML <input>标签，其中type属性设置 为checkbox%>

<sf:option>         <%渲染成一个HTML <option>标签，其selected属性根据所绑定 的值进行设置%>
<sf:options>        <%按照绑定的集合、数组或Map，渲染成一个HTML <option>标 签的列表%>

<sf:select>         <%渲染为一个HTML <select>标签%>
<sf:textarea>       <%渲染为一个HTML <textarea>标签%>
```

**示例**：  
```jsp
<sf:form method="POST" commandName="spitter">
    First Name: <sf:input path="firstName" /><br/>
    Last Name: <sf:input path="lastName" /><br/>
    Email: <sf:input path="email" /><br/>
    Username: <sf:input path="username"/><br/>
    Password: <sf:password path="password"/><br/>
    <input type="submin" value="Register"/>
</sf:form>
```
```java
@Controller
public class ExampleController{
    @RequestMapping(value = "/register", method="GET")
    public String showRegistrationForm(Model model){
        model.addAttrbiute(new Spitter());
        return "registerFrom";//上述JSP。  
    }
}
```

**`<sf:form>`**:  
> `<sf:form>`会渲染会一个HTML<form>标签，但它也会通过`commandName`属性构建针对某个模型对象的上下文信息。  
> 在其他的 表单绑定标签中，会引用这个模型对象的属性。  
> `commandName`属性设置为`spitter`。因此，在模型中必须要有一个**`key`为`spitter`的对象**，否则的话，表单不能正常渲染（会出现JSP错误）。    

**`<sf:input>`**:  
> `<sf:input>`标签会渲染成一个`HTML<input>`标签，并且`type`属性将会设置为`text`。  
> `<input>`标签的`value`属性值将会设置为模型对象中`path`属性所对应的值。  

**`<sf:password>`**:  
> `<sf:password>`来代替`<sf:input>`会将type属性设置为password，这 样当输入的时候，它的值不会直接明文显示。  

**`<sf:input>`的type属性**：  
> 从Spring 3.1开始，`<sf:input>`标签能够允许我们指 定type属性，  
> 这样的话，除了其他可选的类型外，还能指定HTML5 特定类型的文本域，如date、range和email。    

##### 展示表单输入的校验错误   

**`<sf:form>`错误处理**： 
> 相对于标准的HTML标签，使用Spring的表单绑定标签能够带来一定的功能提升，   
> 在校验失败后，表单中会预先填充之前输入的值。  

**`<sf:errors>`展现错误**：  
> 如果存在校验错误的话，请求中会包含错误的详细信息，这些信息是与模型数据放到一起的。    
> `<sf:errors>`能将这些错误信息从模型中抽取出来，并展现给用户。  
> `path`指定展现模型对象中哪个属性的错误，若无错则不渲染任何内容，若有错则在一个HTML<span>标签中显示错误信息。  
```jsp
<sf:form method="POST" commandName="spitter">
    First Name: <sf:input path="firstName"/>
    <sf:errors path="firstName" /><br/>
</sf:form>
```

**修改`<sf:errors>`错误样式**：  
> `cssClass`属性用来修改样式。  
```jsp
<sf:form method="POST" commandName="spitter">
    First Name: <sf:input path="firstName"/>
    <sf:errors path="firstName" cssClass="error"/><%现在errors的<span>会有一个值为error的class属性，解析来需要为其定义CSS样式%>
</sf:from>
```
```css
span.error{
    color:red;
}
```

**集中显示错误**：  
> `<sf:errors>`的`path`属性值`*`是一个通配符选择，表示展现所有属性的所属错误。  
> `<sf:errors>`的`element`属性值`div`表示将错误渲染在一个`div`中（用块级元素展现多个错误更好，而不是用`<span>`）。  
```jsp
<sf:form method="POST" commandName="spitter">
    <sf:errors path="*" element="div" cssClass="errors"/>
</sf:form>
```
```css
div.errors{
    background-color: #ffccc;
    border:2px solid red;
}
```

**着重显示需要修正的域**：  
> 通过设置`cssErrorClass`属性，可以着重显示需要修正的域，通过`path`属性选择模型对象的属性。  
> 当校验有错误的时候，渲染的结果HTML标签的`class`属性设置为`cssErrorClass`属性的值。  
> 通过定义样式就能在出现错误的时候，改变显示样式，从而着重显示需要修正的域。    
> `<sf:label>`标签和`<sf:input>`标签都可以着重显示需要修正。  
```jsp
<sf:form method="POST" commandName="spitter">
    <sf:label path="firstName" cssErrorClass="error"> First Name </sf:label>: 
    <sf:input path="firstName" cssErrorClass="error"/><br/>
</sf:form>
```
```css
label.error{
    color:red;
}
input.error{
    background-color:#ffccc;
}
```

**校验消息**：  
> 现在，我们有了很好的方式为用户展现错误信息。不过，我们还可以 做另外一件事情，能够让这些错误信息更加易读。  
> 我们可以在校验注解上设置`message`属性，使其引用对用户更为友好的信息，而这些信息可以定义在属性文件中。   
> `message`属性:
> 如果没有大括号的话，`message`中的值将会作为展现给用户的错误信息。  
> 但是使用了大括号之后，我们使用的就是属性文件中的某一个属性，该属性包含了实际的信息。   
> `ValidationMessages.properties`文件
> 接下来需要做的就是创建一个名为`ValidationMessages.properties`的文 件，并将其放在根类路径之下。  
> ValidationMessages.properties文件中每条信息的key值对应于注解中 message属性占位符的值。  
> 这个用户友好的信息中也有占位符——`{min}`和`{max}`——它们会引用`@Size`注解上所设置的`min`和`max`属性。  
> 
```java
public class Spitter {
    private Long id;

    @NotNull
    @Size(min=5, max=16, message="{username.size}")
    private String userName;

    @NotNull
    @Size(min=5, max=25, message="{password.size}")
    private String password;

    @NotNull
    @Size(min=5, max=30, message="{firstName.size}")
    private String firstName;

    @NotNull
    @Size(min=2, max=30, message="{lastName.size}")
    private String lastName;

    //...
}
```
```properties
firstName.size = First name ust be between {min} and {max} characters long. 
lastName.size =  Last name must be between {min} and {max} characters long.
username.size = Username must be between {min} and {max} character long.
password.size = Password must be between {min} and {max} characters long. 
email.valid = The email address must be valid.
```

**属性文件的好处**：  
> 将这些错误信息抽取到属性文件中还会带来一个好处，那就是我们可 以通过创建地域相关的属性文件，为用户展现特定语言和地域的信 息。  
> 例如，如果用户的浏览器设置成了西班牙语，那么就应该用西班 牙语展现错误信息，我们需要创建一个名为Validation- Errors_es.properties的文件 

##### Spring的通用标签库 
> 一些标签已经被Spring表单绑定标签库淘汰了。  
> 例如，`<s:bind>`标签就是Spring最初所提供的表单绑定标签，它比我 们在前面所介绍的标签复杂得多。  
> 
**标签库**：   
```jsp
<%@ taglib prefix="s" uri="www.springframework.org/tags" %> 

<s:bind>            <% 将绑定属性的状态导出到一个名为status的页面作用域属性 中，与<s:path>组合使用获取绑定属性的值 %> 
<s:escapeBody>      <% 将标签体中的内容进行HTML和/或JavaScript转义 %>
<s:hasBindError>    <% 根据指定模型对象（在请求属性中）是否有绑定错误，有条 件地渲染内容 %>
<s:htmlEscape>      <% 为当前页面设置默认的HTML转义值%>
<s:message>         <% 根据给定的编码获取信息，然后要么进行渲染（默认行 为），要么将其设置为页面作用域、请求作用域、会话作用 域或应用作用域的变量（通过使用var和scope属性实现）%>
<s:netedPath>       <% 设置嵌入式的path，用于<s:bind>之中 %>
<s:theme>           <% 根据给定的编码获取主题信息，然后要么进行渲染（默认行 为），要么将其设置为页面作用域、请求作用域、会话作用 域或应用作用域的变量（通过使用var和scope属性实现） %>
<s:trasform>        <% 使用命令对象的属性编辑器转换命令对象中不包含的属性 %>
<s:url>             <% 创建相对于上下文的URL，支持URI模板变量以及 HTML/XML/JavaScript转义。可以渲染URL（默认行为），也 可以将其设置为页面作用域、请求作用域、会话作用域或应 用作用域的变量（通过使用var和scope属性实现） %>
<s:eval>            <% 计算符合Spring表达式语言（Spring Expression Language， SpEL）语法的某个表达式的值，然后要么进行渲染（默认行 为），要么将其设置为页面作用域、请求作用域、会话作用 域或应用作用域的变量（通过使用var和scope属性实现） %>
```


**展示国际化信息**：  
> `<s:message>`用于国际化消息，为不同的地区的用户展示相同的内容的国际化表示。  
> 将`code`属性的值作为键，在信息源中获取国际化消息。  
> Spring有多个信息源的类，这些类都是实现了`MessageSource`接口。 
> `ResourceBundleMessageSource`会从一个属性文件加载信息，属性文件的名称是根据基础名称衍生来（添加locale表示字符串表示用于不同地区的国际化信息。如_zh_CN表示中国）。    
> `ReloadableResourceBundleMessageSource`非常类似上一个(另一个可选方案)，但是它能够重新加载信息属性，而**不必重新编译或重启应用**。  

```jsp
<%@ taglib prefix="s" uri="http://www.spingframework.org/tags" %>
<s:message code="spitter.welcom"/ >
```
```java
@Configuration 
@ComponentScan(basePacakgeClasses={ExampleConfiguration.class})
public class ExampleConfiguration{
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("message");
        nreturn messageSource;
    }
    
    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messsageSource = new ReloadableResourceBundleMessageSource();
        //关键区别在于basename属性设置为在应用的外部查找。 
        //basename属性可以设置为在类路径下（以“classpath:”作 为前缀）、文件系统中（以“file:”作为前缀）或Web应用的根路径 下（没有前缀）查找属性。
        messageSource.setBasename("file:///etc/spitter/messages");  
        
        messageSource.setCacheSeconds(10);
        return messageSource;
    }
}
```
```properties
## 默认的messages.properties文件 
spittr.welcome = Welcome to Spittr!
```
```properties
## 中国messages_zh_CN.properties文件， 
spittr.welcom = 欢迎来到spittr!
```

**`<s:url>`创建URI**:  
- `href`属性基本功能： 接受一个相对于Servlet上下文的URI，在渲染是添加上Servlet上下文路径。 不必在担心Servlet上下文路径。  
- `var`属性复制变量： 可以将其赋值个一个变量并在后续用`${}`来使用这个变量。  
- `scope`属性设置作用域： 默认URL在页面作用域内创建，可以通过`scope`属性设置应用作用域、会话作用域或请求作用域内创建URL。  
- `<s:param>`子元素： 添加URL参数。  
- `URL`占位符设置路径变量：当href属性中的占位符匹配`<s:param>`中所指定的参数时，这个参数将会插入到占位符的位置中。    
- `htmlEscape`属性：`true`表示转义，将把URL内容展现在Web页面上。  
- `javaScriptEscape`属性：`true`值表示在`JavaScript`代码中使用URL。 
```jsp
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<s:url href="/spitter/register" var="registerUrl" scope="page" />
<a href="${registerUrl}">Rgister</a>

<s:url href="/spitter/register/{username}">
    <s:param name="max" value="60"/>
    <s:param name="min" value="20"/>
    <s:param name="username" value="jabuer"/>
</s:url>

<s:url value="/spittles" htmlEscape="true"/>

<s:url value="/spittles" var="spittlesJSUrl" javaScriptEscape="true">
    <s:param name="max" value="60"/>
    <s:param name="count" value="20"/>
</s:url>
<script>
    var spittlesUrl = "${spittlesJSUrl}"
</script>
```

**`<s:escapeBody>`通用转义标签**：  
> 它会渲染标签体中 内嵌的内容，并且在必要的时候进行转义。
- `javaScriptEscape`: 通过设置`javaScriptEscape`属性，`<s:escapeBody>`标签还支持 JavaScript转义。   


