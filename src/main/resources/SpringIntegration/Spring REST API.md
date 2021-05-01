## Spring REST API 

**REST与SOAP**： 
> 近几年来，以信息为中心的表述性状态转移（Representational State Transfer，REST）已成为替换传统SOAP Web服务的流行方案。  
> SOAP 一般会关注行为和处理，而REST关注的是要处理的数据。  

**Spring对REST的支持**：  
> Spring对REST的支持是构建在Spring MVC之上的。  
> 从Spring 3.0版本开始，Spring为创建REST API提供了良好的支持。   
> Spring的REST实现在Spring 3.1、3.2和如今的4.0版本中不断得到发 展。   

### 了解REST  

#### REST基础知识  

**REST面向资源**:  
> RPC是面向服务的，并关 注于行为和动作；  
> 而REST是面向资源的，强调描述应用程序的事物 和名词。  

**REST误解**： 
> 当谈论REST时，有一种常见的错误就是将其视为“基于URL的Web服 务”  
> ——将REST作为另一种类型的远程过程调用（remote procedure call，RPC）机制，  
> 就像SOAP一样，只不过是通过简单的HTTP URL 来触发，而不是使用SOAP大量的XML命名空间。   

**REST定义**： 
> 为了理解REST是什么，我们将它的首字母缩写拆分为不同的构成部分。  
> 更简洁地讲，REST就是将资源的状态以最适合客户端或服务端的形 式从服务器端转移到客户端（或者反过来）。  
- 表述性（Representational）： REST资源实际上可以用各种形式来进行表述。  
    > 包括XML、JSON（JavaScript Object Notation）,  
    > 甚至HTML——最适合资源使用者的任意形式。  
- 状态（State）：当使用REST的时候，我们更关注资源的状态而 不是对资源采取的行为。  
- 转移（Transfer）： REST涉及到转移资源数据，它以某种表述性 形式从一个应用转移到另一个应用。  

**REST资源**通过**URI**识别和定位资源：  
> 在REST中，资源通过URL进行识别和定位。  
> 至于RESTful URL的结构 并没有严格的规则，但是URL应该能够识别资源，而不是简单的发一条命令到服务器上。  
> 再次强调，关注的核心是事物，而不是行为。  

**REST行为**通过**HTTP**方法定义行为：  
> REST中会有行为，它们是通过HTTP方法来定义的。  
> 具体来讲，也就 是GET、POST、PUT、DELETE、PATCH以及其他的HTTP方法构成了 REST中的动作。  
> 这些HTTP方法通常会匹配为如下的CRUD动作:  
- Create : POST.  
- READ: GET.  
- Update: PUT 或 PATCH.  
- Delete: DELETE.  

#### Spring使用REST   

**创建REST资源的方式**： 
> Spring很早就有导出REST资源的需求。  
> 从3.0版本开始，Spring针对 Spring MVC的一些增强功能对REST提供了良好的支持。  
> 当前的4.0版 本中，Spring支持以下方式来创建REST资源：  
- `@RequestMapping`注解**配置行为**： 
    > 控制器可以处理**所有的HTTP**方法。  
    > 包括`GET`、`PUT`、`DELETE`、`POST`，Spring3.2以上还支持`PATCH`。  
- `@PathVariabel`注解**配置资源**：  
    > 使控制器能够处理**参数化的URL**（将变量输入作为URL的一部分）。  
- 视图和视图解析**表述资源** ： 
    > 视图和视图解析器将资源表示为多种方式： 包括将模型数据渲染为XML、JSON、Atom以及RSS的View 实现. 
- **协商资源表述**方式：  
    > `ContentNegotiatingViewResolver`协商资源表述的方式。  
- 替换基于视图的渲染方式： 
    > 借助`@ResponseBody`注解和各种`HttpMethodConverter`实现，能够替换基于视图的渲染方式。  
- 转化传入的HTTP数据： 
    > 类似地，@RequestBody注解以及HttpMethodConverter实 现可以将传入的HTTP数据转化为传入控制器处理方法的Java对象。    
- 借助`RestTemplate`，Spring应用能够方便地使用REST资源。  

### 创建REST端点   

**控制器以Java方式处理资源**： 
> 控制器本身通常并不关心资源如何表述。控制器以Java 对象的方式来处理资源。  
> 控制器完成了它的工作之后，资源才会被转化成最适合客户端的形式。  

**Spring转化资源表述的方式**：  
> Spring提供了两种方法将资源的Java表述形式转换为发送给客户端的表述形式。  
- 内容协商：选择一个视图，它能够将模型 渲染为呈现给客户端的表述形式。   
- 消息转换器：通过一个消息转换器将控制器所返回的对象转换为呈现给客户端的表述形式。  

#### 内容协商  

**`ContentNegotiatingViewResolver`实现内容协商**：  
> Spring的ContentNegotiatingViewResolver是一个特殊的视图解析器。  
> 它根据客户端所需要的内容类型，选择对应的视图解析器来转换资源的表述方式。  

**内容协商的步骤**：  
- 确定请求的媒体类型。  
- 找到适合请求媒体的最佳视图。  

**确定请求的媒体类型**：  
> 需启动使用扩展名，才能根据URL文件扩展名确定媒体类型，具体看ContentNegotiationManager.
- 首先查看请求URL的文件扩展名。  
- 若URL结尾有文件扩展名，则查看相应的类型`Content-Type`，内容类型必须匹配文件扩展名。   
    > 如果扩展名是“.json”的话，那么所需的内容类型必须 是“application/json”。  
- 若不能根据文件扩展名获得媒体类型，则考虑请求的`Accept`头信息。  
- 如果没有`Accept`头信息，则使用`/`作为默认的内容类型， 客户端必须接收服务器发送的任何内容类型。   

**找到适合请求媒体的最佳视图**：  
> 一旦内容类型确定之后，ContentNegotiatingViewResolver就 该将逻辑视图名解析为渲染模型的View。  
> ContentNegotiatingViewResolver本身不会解析视图。 而是委托给其他的视图解析器，让它们来解析视图。
- 要求其他的视图解析器将逻辑视图名解析为视图并放入列表中。  
- `ContentNegotiatingViewResolver`会循环客户端请求的所有媒体类型，在候选的视图中查找能够产生对应内容类型的视图。  
- **第一个匹配**的视图会用来**渲染模型**。  

**`ContentNegotiationManager`影响媒体类型的选择**：  
> 通过为`ContentNegotiatingViewResolver`设置一个ContentNegotiationManager。  
> 可以改变确定请求媒体的策略。  
> `ContentNegotiationManager`能做到如下事情：  
- 指定**默认的**内容类型： 如果根据请求无法得到内容类型的话，将会使用默认值。  
- 通过**URL扩展名**指定内容类型。 
- 通过**请求参数**指定内容类型。  
- **忽视请求的`Accept`**头部信息。  
- 将**请求的扩展名**映射为特定的**媒体类型**。  
- 将JAF（Java Activation Framework）作为根据扩展名查找媒体类 型的备用方案。  

**配置`ContentNegotitionManager`**:  
- 直接声明一个**`ContentNegotiationManager`**类型的bean。  
- 通过**`ContentNegotiationManagerFactoryBean`**间接创建 bean。  
- 重载**`WebMvcConfigurerAdapter`**的**`configureContentNegotiation()`**方法。  

**配置`ContentNegotitionManager`的建议**： 
> 直接创建ContentNegotiationManager有一些复杂，除非有充分 的原因，否则我们不会愿意这样做。  
> 后两种方案能够让创建 ContentNegotiationManager更加简单。   

**直接配置`ContentNegotiatingViewResolver`**:  
> ContentNegotiationManager是Spring中相对比较新的功能，是在Spring 3.2中引入的。  
> 在Spring 3.2之 前，ContentNegotiatingViewResolver的很多行为都是通 过直接设置ContentNegotiatingViewResolver的属性进行 配置的。  
> 从Spring 3.2开始，Content- NegotiatingViewResolver的大多数Setter方法都废弃了，鼓 励通过Content-NegotiationManager来进行配置。    

**控制器示例**： 
```java
package pers.mortal.learn.springintegration.restapi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RESTController {

    @RequestMapping("/rest/point")
    public String getIndex(){
        return "home";
    }

    @RequestMapping("/rest/beanView/{file}")
    public String getBeanView(Model model){
        model.addAttribute("num", new int[]{0,1,2,3,4,5,6,7,8,9});
        model.addAttribute("str",new String[]{"0","1","2","3","4","5","6","7","8","9"});
        return "beanView";
    }
}
```

**配置示例**： 
```java
package pers.mortal.learn.springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import pers.mortal.learn.springdata.security.ExampleMethodSecurityController;
import pers.mortal.learn.springintegration.restapi.RESTController;
import pers.mortal.learn.springmvc.multipart.ExampleMultipartConfig;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {WebConfig.class, ExampleMethodSecurityController.class, RESTController.class})
@Import({ExampleMultipartConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver irViewResolver(){
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();

        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);

        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }

    //配置ContentNegotiationManager
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
         //默认内容类型。
                configurer.defaultContentType(MediaType.TEXT_HTML);
                //根据扩展名指定内容类型
                configurer.favorPathExtension(true);
                //根据请求参数指定内容类型。
                configurer.favorParameter(true);
                configurer.parameterName("format");//设置确当内容类型的请求参数名，默认为format
                //忽视请求的Accept头信息。
//                configurer.ignoreAcceptHeader(true);
                //将请求的扩展名映射为特定的媒体类型
                configurer.mediaTypes(new HashMap<String, MediaType>(){{
                    put("my_type", MediaType.APPLICATION_JSON);
                }});
    }
    //配置ContentNegotiatingViewResolver
    @Bean
    public ViewResolver cnViewResolver(ContentNegotiationManager contentNegotiationManager, List<ViewResolver> viewResolvers){
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(contentNegotiationManager);
        resolver.setViewResolvers(viewResolvers);
        return resolver;
    }
    //配置其他ViewResolver.
    @Bean ViewResolver beanNameViewResolver(){
        return new BeanNameViewResolver();
    }
    //Bean视图
    @Bean
    public View beanView(){
        return new MappingJackson2JsonView();
    }
}
```

#### 消息转换器   
**消息转换器**： 
> 消息转换（message conversion）提供了一种更为直接的方式，它能够将控制器产生的数据转换为服务于客户端的表述形式。  
> 五个消息转换器都是自动注册，不需要Spring配置。  
> 但是为了支持它们，你需要添加一些库到应用程序的类路径下。  
> 根据客户端请求的Accept头信息决定将返回对象转换为何种形式。  

|消息转换器|描述|  
|:---|:---|
|`AtomFeedHttpMessageConverter`|Rome Feed对象和Atom feed（媒体类型 application/atom+xml）之间的互相转 换。如果 Rome 包在类路径下将会进行注册|  
|`BufferedImageHttpMessageConverter`|BufferedImages与图片二进制数据之间互 相转换|  
|`ByteArrayHttpMessageConverter`|读取/写入字节数组。从所有媒体类型 （*/*）中读取，并以application/octet- stream格式写入|
|`FormHttpMessageConverter`|将application/x-www-form-urlencoded内 容读入到MultiValueMap<String,String> 中，也会 将MultiValueMap<String,String>写入 到application/x-www-form- urlencoded中 或将MultiValueMap<String, Object>写入 到multipart/form-data中|
|`Jaxb2RootElementHttpMessageConverter`| 在XML（text/xml或application/xml）和 使用JAXB2注解的对象间互相读取和写 入。如果 JAXB v2 库在类路径下，将进行注 册|
|`MappingJacksonHttpMessageConverter`|HashMap间互相读取和写入。 如果 Jackson JSON 库在类路径下，将进 行注册|
|`MappingJackson2HttpMessageConverter`|在JSON和类型化的对象或非类型化的 HashMap间互相读取和写入。 如果 Jackson 2 JSON 库在类路径下，将 进行注册|
|`MarshallingHttpMessageConverter`|使用注入的编排器和解排器（marshaller 和unmarshaller）来读入和写入XML。支 持的编排器和解排器包括Castor、 JAXB2、JIBX、XMLBeans以及Xstream|
|`ResourceHttpMessageConverter`|读取或写入Resource|
|`RssChannelHttpMessageConverter `| 在RSS feed和Rome Channel对象间互相读 取或写入。 如果 Rome 库在类路径下，将进行注册|
|`SourceHttpMessageConverter`|在XML和javax.xml.transform.Source对 象间互相读取和写入。 默认注册|
|`StringHttpMessageConverter`| 将所有媒体类型（*/*）读取为String。 将String写入为为text/plain|
|`XmlAwareFormHttpMessageConverter`|FormHttpMessageConverter的扩展，使 用SourceHttp MessageConverter来支持基 于XML的部分|

**使用消息转换器**：  
- `@RequestMapping`注解：
    - `produces=xxx`属性：表明这个方法只处理预期输出为xxx的请求，只会处理Accept头部信息含有xxx的请求。  
    > 其他任何类型的请求即便匹配URL和HTTP方法，也不会被这个处理器方法处理。  
    > 如果有其他的合适方法则交由合适的方法处理，否则返回回客户端HTTP 406（Not Acceptable）响应。  
    > 即使通过了produces检查，若无法将返回对象转化为合适的表述形式，也会返回406.
    > 若同时开启了内容协商，则内容协商也会对produces检查，若无Accept则检查用默认内容类型来检查produeces；若禁止Accept，则produces检查一定失败。  
    - `consumes=xxx`属性：表明这个方法只处理请求头`Content-Type=xxx`的请求，否则有其他方法处理，或返回406.  
- `@ResopnseBody`注解处理器返回值为消息体： 告诉Spring，将返回的对象作为资源发送个客户端，并转换为客户端可接收的形式。  
- `@RequestBody`注解处理器参数：，告诉Spring查找一个消息转换器，将来自客户端的资源表述转换为对象。  
- `@RestController`注解控制器可为处理器方法省略`@ResponseBody`(Spring4.0引入)：
    > 如果在控制器类上使用`@RestController`来代替`@Controller`的话，  
    > Spring将会为该控制器的所有处理方法应用消息转换功能。  
    > 不必为每个方法都添加@ResponseBody了。  
                                                                 
**Jackson默认会使用反射**：    
> 注意在默认情况下，Jackson JSON库在将返回的对象转换为JSON资源 表述时，会使用反射。  
> 是如果你重构了Java类型，比如添加、移除或重命名属性，那么所产生的JSON也将会发生变化（如果客户端依赖这些属性的话，那客 户端有可能会出错）。  
> 但是，我们可以在Java类型上使用Jackson的映射注解，从而改变产生 JSON的行为。  
> 这样我们就能更多地控制所产生的JSON，从而防止它 影响到API或客户端。  

**示例**：  
```java
package pers.mortal.learn.springintegration.restapi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RESTController {

    //@ResponseBody将方法结果作为消息体转化为客户端合适的类型。
    @RequestMapping(value = "/rest/response_body", method = RequestMethod.GET
            ,produces = "application/json"
    )
    public @ResponseBody
    List<Integer> useProduces(){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(i);
        }
        return list;
    }

    //@RequestBody将客户端消息转化为参数对象。
    @RequestMapping(value = "/rest/request_body", method = RequestMethod.POST
            , produces = {"application/json"}
            , consumes = {"application/json"})
    public @ResponseBody List<Integer> useConsumes(@RequestBody List<Integer> list){
        return list;
    }
}

@RestController//省略@ResponseBody
public class RESTController {

    //@ResponseBody将方法结果作为消息体转化为客户端合适的类型。
    @RequestMapping(value = "/rest/response_body", method = RequestMethod.GET
            ,produces = "application/json"
    )
    public List<Integer> useProduces(){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(i);
        }
        return list;
    }

    //@RequestBody将客户端消息转化为参数对象。
    @RequestMapping(value = "/rest/request_body", method = RequestMethod.POST
            , produces = {"application/json"}
            , consumes = {"application/json"})
    public List<Integer> useConsumes(@RequestBody List<Integer> list){
        return list;
    }
}
```

### 提供资源之外的其他内容  

#### 发送错误信息到客户端  

**元数据**： 
> @ResponseBody提供了一种很有用的方式，能够将控制器返回的Java对象转换为发送到客户端的资源表述。  
> 实际上，将资源表述发送 给客户端只是整个过程的一部分。  
> 一个好的REST API不仅能够在客 户端和服务器之间传递资源，它还能够给客户端提供额外的元数据，   
>帮助客户端理解资源或者在请求中出现了什么情况。    

> Spring提供了多种方式来处理REST错误。  
- **`@ResponseStatus`注解**指定状态码：  
- 控制器方法返回**`ResponseEntiry`对象**，包含更多元数据。  
- 异常处理器：这样子，处理器方法就能专注于正常状况。  

**使用`ResponseEntiry`**：  
> 作为@ResponseBody的替代方案，控制器方法可以返回一 个ResponseEntity对象。  
> ResponseEntity中可以包含响应相关的元数据（如头部信息和状态码）以及要转换成资源表述的对象。  
> 除了包含响应头信息、状态码以及负载以外，ResponseEntity还包含了 @ResponseBody的语义，  
> 因此负载部分将会渲染到响应体中，就像 之前在方法上使用@ResponseBody注解一样。   
> 如果返回 ResponseEntity的话，那就没有必要在方法上使用@ResponseBody注 解了。  
```java
package pers.mortal.learn.springintegration.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RESTController {
     private List<Integer> list = new ArrayList<>();
     {
         for(int i = 0; i < 10; i++){
             list.add(i);
         }
     }
 
    //使用ResponseEntity代替@ResponseBody
    @RequestMapping(value = "/rest/response_entity")
    public ResponseEntity<?> useResponseEntity(@RequestParam("code") int code){
        HttpStatus status ;
        ResponseEntity<?> entity;
        if(code == 200){//返回资源。
            status = HttpStatus.OK;
            entity = new ResponseEntity<>(list, status);
        }else{//使用自定义的包含错误信息的Error对象。
            status = HttpStatus.NOT_FOUND;
            Error error = new Error(404, "not found");
            entity = new ResponseEntity<>(error, status);
        }

        return entity;
    }
}
class Error{
    private int code;
    private String message;
    public Error(int code, String message){
        this.code = code;
        this.message = message;
    }
    public int getCode(){
        return this.code;
    }
    public String getMessage(){
        return this.message;
    }
}
```

**异常处理器`@ExceptionHandler`**： 
> 使用了ResponseEntity后，控制器逻辑比我们开始的时候更为复杂。  
> 涉及到了更多的逻辑，包括条 件语句。  
> 另外，方法返回ResponseEntity<?>感觉有些问 题。   
> ResponseEntity所使用的泛型为它的解析或出现错误留下了 太多的空间。  
>  不过，我们可以借助错误处理器来修正这些问题。  
```java
package pers.mortal.learn.springintegration.restapi;
 
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.*;
 
 import java.util.ArrayList;
 import java.util.List;
 
 @Controller
 public class RESTController {
     private List<Integer> list = new ArrayList<>();
     {
         for(int i = 0; i < 10; i++){
             list.add(i);
         }
     }
 
     //使用ResponseEntity代替@ResponseBody
     @RequestMapping(value = "/rest/response_entity")
     public ResponseEntity<?> useResponseEntity(@RequestParam("code") int code){
         HttpStatus status ;
         ResponseEntity<?> entity;
         if(code == 200){//返回资源。
             status = HttpStatus.OK;
             entity = new ResponseEntity<>(list, status);
         }else{//使用自定义的包含错误信息的Error对象。
             status = HttpStatus.NOT_FOUND;
             Error error = new Error(404, "not found");
             entity = new ResponseEntity<>(error, status);
         }
 
         return entity;
     }
 
     //使用异常处理器
     @RequestMapping(value = "/rest/exception_handle")
     public ResponseEntity<List<Integer>> useException_handle(@RequestParam("code") int code){
         if(code != 200){
             throw new RESTException();
         }
         
         return new ResponseEntity<>(list, HttpStatus.OK);
     }
     //@Exception注解到控制器方法，能处理控制器抛出的特定异常。
     @ExceptionHandler(RESTException.class)
     public ResponseEntity<Error> exceptionHandler(RESTException restException){
         Error error = new Error(404, "not found");
         return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
     }
     
     
 }
 class RESTException extends RuntimeException{
 }
 class Error{
     private int code;
     private String message;
     public Error(int code, String message){
         this.code = code;
         this.message = message;
     }
     public int getCode(){
         return this.code;
     }
     public String getMessage(){
         return this.message;
     }
 }
```

**更干净的代码**： 
- 控制器方法干净：
    - 使用了异常处理器后，控制器方法就不必需要`ResponseEntity`，可以换回`@ResponseBody`。  
    - 若使用了`@RestController`后，连`@ResponseBody`也不需要了，代码更干净。  
- 异常处理方法更干净：
    - 错误处理方法始终返回Error，故也不必使用`@ResponseEntiry`,可以换回`@ResponseBody`。   
    - 若使用了`@RestController`，也不必使用`@ResponseBody`。 
    - 为了使HTTP状态码为404，需添加**`@ResponseStatus(HttpSatus.NOT_FOUND)`**注解。
```java
package pers.mortal.learn.springintegration.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RESTController {
    private List<Integer> list = new ArrayList<>();
    {
        for(int i = 0; i < 10; i++){
            list.add(i);
        }
    }
   
    //使用异常处理器
    @RequestMapping(value = "/rest/exception_handle")
    public List<Integer> useException_handle(@RequestParam("code") int code){
        if(code != 200){
            throw new RESTException();
        }

        return list;
    }
    //@Exception注解到控制器方法，能处理控制器抛出的特定异常。
    @ExceptionHandler(RESTException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error exceptionHandler(RESTException restException){
        Error error = new Error(404, "not found");
        return error;
    }


}
class RESTException extends RuntimeException{
}
class Error{
    private int code;
    private String message;
    public Error(int code, String message){
        this.code = code;
        this.message = message;
    }
    public int getCode(){
        return this.code;
    }
    public String getMessage(){
        return this.message;
    }
}
```

#### 在响应中设置头部信息   

**用`@Responseentity`设置响应头部信息,`UriComponentsBuilder`构建URI**：  
> 当创建新资源的时候，将资源的URL放在响应的Location头部信息 中，并返回给客户端是一种很好的方式。   
> 因此，我们需要有一种方式 来填充响应头部信息，此时我们的老朋友ResponseEntity就能提供帮助了。  
```java
package pers.mortal.learn.springintegration.restapi;
 
 import org.springframework.http.HttpHeaders;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.util.UriComponentsBuilder;
 
 import java.net.URI;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 
 @Controller
 public class RESTController {
 
     @RequestMapping(
             value = "/rest/resource",
             method = RequestMethod.POST,
             consumes = {"application/json"}
     )
     public ResponseEntity<Integer> saveList(@RequestBody List<Integer> list, UriComponentsBuilder ucb){
         //保存资源
         int id = ExampleResource.save(list);
         //设置头部信息
             //HttpHeaders 是MultiValueMap<String, String>的特殊实现，
             // 它有一些便 利的Setter方法（如setLocation()），用来设置常见的HTTP头部 信息。
         HttpHeaders headers = new HttpHeaders();
             //硬编码构建URI容易出错，应该使用UriComponentsBuilder
         //URI location = URI.create("http://location:8080/Learn_SpringMVC/rest/resource/" + id);
             //Spring提供UriComponentsBuilder，通过构建者模式构建URI，使用它需要在处理器方法中将其作为一个参数。
             //在处理器方法所得到的UriComponentsBuilder中，会预先配置已 知的信息如host、端口以及ServletContext。
             //路径的构建分为两步。第一步调用path()方法，将其设置控制器所处理的基础路径。
             //在第二次调用path()的时候，使用了已保存的id。 
             //可以推断出，每次调用path()都会基于上次调用的结果。
         URI location = ucb.path("/rest/resource/")
                 .path(String.valueOf(id))
                 .build()
                 .toUri();
         headers.setLocation(location);
         //创建ResponseEntity
         ResponseEntity<Integer> responseEntity = new ResponseEntity<>(id, headers, HttpStatus.CREATED);
 
         return responseEntity;
     }
     
     @RequestMapping(
             value = "/rest/resource/{id}"
             ,produces = {"application/json"}
     )
     public @ResponseBody List<Integer> getList(@PathVariable("id") int id){
         return ExampleResource.get(id);
     }
 }
 class ExampleResource{
     public static Map<Integer, List<Integer>> map = new HashMap<>();
     public static int id = 0;
 
     public static int save(List<Integer> list){
         id++;
         map.put(id, list);
         return id;
     }
 
     public static List<Integer> get(int id){
         return map.get(id);
     }
 }
```

### 编写REST客户端  

**`RestTemplate`**:  
> RestTemplate定义了36个与REST资源交互的方法，其中的大多数 都对应于HTTP的方法。  
> 其实，这里面只有11个独立的方法，其中有十个有三种 重载形式，而第十一个则重载了六次。  
> 除了TRACE以外，RestTemplate涵盖了所有的HTTP动作。  
> execute()和exchange()提供了较低层次的通用方法来使用 任意的HTTP方法。 

**`RestTemplate`方法大多数以三种形式进行了重载**：  
- 使用`java.net.URI`作为URI格式，不支持参数话URL。  
- 使用`String`作为URL格式，并使用`Map`指明URI参数。  
- 使用`String`作为URL格式，并使用可变参数列表知名URI参数。  

**`RestTemplate`API**:  
```java
package org.springframework.web.client.RestTemplate;

public class RestTemplate{
    //在URL上执行特定的HTTP方法，返回包含对象的 ResponseEntity，这个对象是从响应体中映射得到的。  
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType){}
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> responseType);
    public <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> responseType, Object... uriVariables);
    public <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> responseType, Map<String, ?> uriVariables);
   
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType);
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> responseType);
    public <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> responseType,  Object... uriVariables);
    public <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables);
    
    //在URL上执行特定的HTTP方法，返回一个从响应体映射得到 的对象
    public <T> T execute(URI url, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor);
    public <T> T execute(String url, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... uriVariables);
    public <T> T execute(String url, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables);
    
    //在特定的URL上对资源执行HTTP DELETE操作
    public void delete(URI uri);
    public void delete(String uri, Object... uriVariables);
    public void delete(String uri, Map<String, ?> uriVariables);
    
    //发送一个HTTP GET请求，返回的ResponseEntity包含了响应体 所映射成的对象
    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType);
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVaraibles);
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables);
    
    //发送一个HTTP GET请求，返回的请求体将映射为一个对象
    public <T> T getForObject(URI url, Class<T> responseType);
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables);
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables);
    
    //发送HTTP HEAD请求，返回包含特定资源URL的HTTP头
    public HttpHeaders headForHeaders(URI uri);
    public HttpHeaders headForHeaders(String uri, Object... uriVariables);
    public HttpHeaders headForHeaders(String uri, Map<String, ?> uriVariables);
    
    //发送HTTP OPTIONS请求，返回对特定URL的Allow头信息
    public Set<HttpMethod> optionsForAllow(URI url);
    public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables);
    public Set<HttpMethod> optionsForAllow(String url, Map<Stirng, ?> uriVariables);
    
    //POST数据到一个URL，返回包含一个对象的ResponseEntity， 这个对象是从响应体中映射得到的
    public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType);
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables);
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables);
    
    //POST数据到一个URL，返回根据响应体匹配形成的对象
    public <T> T postForObject(URI url, Object request, Class<T> responseType);
    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables);
    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables);
    
    //POST数据到一个URL，返回新创建资源的URL
    public URI postForLocation(URI url, Object request);
    public URI postForLocation(String url, Object request, Object... uriVariables);
    public URI postForLocation(String url, Obejct request, Map<String, ?> uriVariables);
    
    //PUT资源到特定的URL
    public void put(URI url, Object request);
    public void put(String url, Object request, Object... uriVariables);
    public void put(String url, Object request, Map<String, ?> uriVariables);
}
```

**`RestTemplate`示例**：  
```java
package pers.mortal.learn.springintegration.restapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class RESTClient {
    private static final String BASE_URI = "http://localhost:8080/learn-springMVC/rest/resource";
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args){
        get(8);
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            list.add(i);
        }
        post(list);
    }

    public static void get(int id){
        String uri = BASE_URI + "/" + id;
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);
        int i = 0;
        i++;
    }

    public static void post(List<Integer> list){
        String uri = BASE_URI;
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(uri, list, Integer.class);
        Integer id = responseEntity.getBody();
        int i = 0;
        i++;
    }
}
```











