## WebSocket  

**`JMS`、`AMQP`、`WebSocket`与异步通信**：   
> 异步消息是应用程序之间通用的交流方式。   
> JMS和AMQP在应用程序之间发送消息。  
> 而WebSocket协议提供了通过一个套接字实现全双工通信的功能。  
> 除了其他的功能之外，它能够实现Web浏览器和服务器之间的异步通信。  

**Spring4.0对WebSocket的支持**:  
- 发送和接受消息的**低层级API**。   
- 发送和接受消息的**高层级API**。  
- 发送消息的**模板**。  
- 支持SockJS，用来解决浏览器端、服务器以及代理不支持WebSocket的问题。  

**WebSocket**: 
> WebSocket通信可以应用于任何类型的应用中，  
> 但是WebSocket最常见 的应用场景是实现服务器和基于浏览器的应用之间的通信。 

### 使用Spring的低层级WebSocket  

**WebSocket消息处理类**: 
> 为了在Spring使用较低层级的API来处理消息，我们必须编写一个实 现WebSocketHandler的类。  
> 实现WebSocketHandler，更为简单的方法是扩 展AbstractWebSocketHandler，这是WebSocketHandler的一 个抽象实现。  
> `TextWebSocketHandler`是`AbstractWebSocketHandler`的子类，拒绝处理二进制消息。 
> 它重载了 handleBinaryMessage()方法，如果收到二进制消息的时候，将 会关闭WebSocket连接。 
> `BinaryWebSocketHandler`是`AbstractWebSocketHandler`的子了，拒绝处理文本消息。
> 它重载了 handleTextMessage()方法，如果收到二进制消息的时候，将 会关闭WebSocket连接。  
> 
- **`WebSocketHandler`接口**:  
- **`AbstractWebSocketHandler`抽象类**：   
- **`TextWebSocketHandler`**： 
- **`BinaryWebSocketHandler`**: 

**WebSocket消息处理类API**:
```java
public interface WebSocketHandler{
    //建立连接后
    void  afterConnectionEstablished(WebSocketSession webSocketSession)throws Exception;
    void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage)throws Exception;
    void handleTransportError(WebSocketSession webSocketSession, Throwable throwable)throws Exception;
    //关闭连接后
    void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus)throws Exception;
    boolean supportsPartialMessages();
}
public abstract class AbstractWebSocketHandler implements WebSocketHandler{
    //handleMessage()方法的具体话，每个方法对应于某一种类型的消息。 
    //所没有重载的方法都 由AbstractWebSocketHandler以空操作的方式（no-op）进行了 实现。 
    public void handleBinaryMessage(WebSocketSession webSocketSession, BinaryMessage message)throws Exception{}
    public void handleTextMessage(WebSocketSession webSocketSession, TextMessage message)throws Exception{}
    public void handlePongMessage(WebSocketSession webSocketSession, PongMessage message)throws Exception{}
    //实现方法
    @Override
    public void  afterConnectionEstablished(WebSocketSession webSocketSession)throws Exception{
    }
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage)throws Exception{
    }
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable)throws Exception{
    }
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus)throws Exception{
    }
    @Override
    public boolean supportsPartialMessages(){
    }
}
public class TextWebSocketHandler extends AbstractWebSocketHandler{
    @Override
    public void handleBinaryMessage(WebSocketSession webSocketSession, BinaryMessage message)throws Exception{
        //关闭连接    
    }
}
public class BinaryWebSocketHandler extends AbstractWebSocketHandler{
    @Override
    public void handleTextMessage(WebSocketSession webSocketSession, TextMessage message)throws Exception{
        //关闭连接    
    }
}
```

**配置使用WebSocket——`@EnableWebSocket`和`WebSocketConfigurer`**：  
> 现在，已经有了消息处理器类，我们必须要对其进行配置，这样 Spring才能将消息转发给它。  
> 。在Spring的Java配置中，这需要在一个配 置类上使用@EnableWebSocket，并实现WebSocketConfigurer 接口。  

```java
package pers.mortal.learn.springintegration.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class ExampleWebSocketConfigurer extends WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        //注册消息处理类Bean并映射到URL。 
        webSocketHandlerRegistry.addHandler(exampleAbstractWebSocketHandler(), "/marco");
    }

    //声明WebSocket消息处理类Bean
    @Bean
    public ExampleAbstractWebSocketHandler exampleAbstractWebSocketHandler(){
        return new ExampleAbstractWebSocketHandler();
    }
}
```
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:websocket="http://www.springframework.org/schema/websocket"
       xsi:schemaLocation=" http://www.springframework.org/schema/beans 
                            http://www.springframework.org/schema/beans/spring-beans.xsd 
                            http://www.springframework.org/schema/websocket 
                            http://www.springframework.org/schema/websocket/spring-websocket.xsd ">
    
    <websocket:handlers>
        <websocket:mapping handler="exampleAbstractWebSocketHandler" path="/marco"/>
    </websocket:handlers>
    
    <bean id="exampleAbstractWebSocketHandler" class="pers.mortal.learn.springintegration.websocket.ExampleAbstractWebSocketHandler"/>

</beans>
```

**编写客户端代码**： 
> URL使用了“ws://”前缀，表明这是一个基本的WebSocket连 接。如果是安全WebSocket的话，协议的前缀将会是“wss://”。  
```html
<!DOCTYPE html>
<html>
    <head>
        <title>WebSocket</title>
        <meta charset="UTF-8"/>
    </head>
    <body>
<!--    <script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>-->
    <script>
            var url = "ws://" + window.location.host + "/learn-springMVC/websocket";
            var sock = new WebSocket(url);

            sock.onopen = function(){
                console.log('Opening');
                sayMarco();
            };

            sock.onmessage = function(e){
                console.log('Received message: ', e.data);
                setTimeout(function(){sayMarco()}, 2000);
            };

            sock.onclose = function(){
                console.log('Closing');
            };

            function sayMarco(){
                console.log('Sending Marco!');
                sock.send("Marco!");
            };
        </script>
    </body>
</html>
```

**运行在Servlet容器**： 
> 到此为止，我们已经编写完使用Spring低层级WebSocket API的所有代 码，包括接收和发送消息的处理器类，以及在浏览器端完成相同功能的JavaScript客户端。  
> 如果我们构建这些代码并将其部署到Servlet容器 中，那它有可能能够正常运行。  

### 应对不支持WebSocket的场景  
> WebSocket是一个相对比较新的规范。虽然它早在2011年底就实现了 规范化， 
> 但即便如此，在Web浏览器和应用服务器上依然没有得到一 致的支持。  
> 即便浏览器和应用服务器的版本都符合要求，两端都支持 WebSocket，在这两者之间还有可能出现问题。  
> 防火墙代理通常会限 制所有除HTTP以外的流量。它们有可能不支持或者（还）没有配置 允许进行WebSocket通信。   

**SockJS——WebSocket的备用方案**  
> SockJS 是WebSocket技术的一种模拟，在表面上，它尽可能对应WebSocket API，  
> 但是在底层它非常智能，如果WebSocket技术不可用的话，就会 选择另外的通信方式。  
> 好消息是在使用SockJS之前，我们并没有必要全部了解这些方案。   
> SockJS让我们能够使用统一的编程模型，就好像在各个层面都完整支 持WebSocket一样，SockJS在底层会提供备用方案。 
- XHR流。   
- XDR流。   
- iFrame事件源。   
- iFrame HTML文件。   
- XHR轮询。   
- XDR轮询。   
- iFrame XHR轮询。   
- JSONP轮询。  

**启用SockeJS**:  
> registerWebSocketHandlers()方法，稍微加一点内容就能启 用SockJS。  
> addHandler()方法会返回WebSocketHandlerRegistration，通过简单地调用其withSockJS()方法就能声明我们想要使用SockJS 功能，如果WebSocket不可用的话，SockJS的备用方案就会发挥作 用。  
> 如果你使用XML来配置Spring的话，启用SockJS只需在配置中添加`<websocket:sockjs>`元素即可。  
```java
package pers.mortal.learn.springintegration.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class ExampleWebSocketConfigurer implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        //注册消息处理类Bean并映射到URL。
        webSocketHandlerRegistry.addHandler(exampleAbstractWebSocketHandler(), "/marco")
        .withSockJS();//addHandler()方法会返回WebSocketHandlerRegistration，通过简单地调用其withSockJS()方法就能声明我们想要使用SockJS 功能，
    }

    //声明WebSocket消息处理类Bean
    @Bean
    public ExampleAbstractWebSocketHandler exampleAbstractWebSocketHandler(){
        return new ExampleAbstractWebSocketHandler();
    }
}
```
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:websocket="http://www.springframework.org/schema/websocket"
       xsi:schemaLocation=" http://www.springframework.org/schema/beans 
                            http://www.springframework.org/schema/beans/spring-beans.xsd 
                            http://www.springframework.org/schema/websocket 
                            http://www.springframework.org/schema/websocket/spring-websocket.xsd ">
    
    <websocket:handlers>
        <websocket:mapping handler="exampleAbstractWebSocketHandler" path="/marco"/>
        <websocket:sockjs/><!-- 如果你使用XML来配置Spring的话，启用SockJS只需在配置中添加`<websocket:sockjs>`元素即可。--> 
    </websocket:handlers>
    
    <bean id="exampleAbstractWebSocketHandler" class="pers.mortal.learn.springintegration.websocket.ExampleAbstractWebSocketHandler"/>

</beans>
```

**客户端使用SockeJS**:  
> 要在客户端使用SockJS，需要确保加载了SockJS客户端库。  
> 具体的做 法在很大程度上依赖于使用JavaScript模块加载器（如require.js或 curl.js）还是简单地使用<script>标签加载JavaScript库。  
> 有两处需要修改： 
> 如果CDN加载失败，去网址https://www.bootcdn.cn/获取更新的链接（可能是因为旧的链接被弃用了）。  
- SockJS客户端库的最简单办法是使用<script>标签从SockJS CDN中 进行加载。  
- SockJS所处理的URL 是“http://”或“https://”模式，而不是“ws://”和“wss://”。

```html
<!DOCTYPE html>
<html>
    <head>
        <title>WebSocket</title>
        <meta charset="UTF-8"/>
    </head>
    <body>
    <script src="https://cdn.bootcdn.net/ajax/libs/sockjs-client/1.5.1/sockjs.js"></script>
    <script>
<!--            var url = "ws://" + window.location.host + "/learn-springMVC/websocket";-->
<!--            var sock = new WebSocket(url);-->

            var url = "http://" + window.location.host + "/learn-springMVC/websocket";
            var sock = new SockJS(url);
        </script>
    </body>
</html>

```


### 使用STOMP消息  
> WebSocket提供了浏览器-服务器之间的通信方式，当运行环境不支持 WebSocket的时候，SockJS提供了备用方案。  
> 但是不管哪种场景，对于实际应用来说，这种通信形式都显得层级过低。  
> 在WebSocket之上使用STOMP（Simple Text Oriented Messaging Protocol），为浏览器-服务器之间的通信增加恰当的消息语义。

**STOMP在WebSocket上提供了基于帧的线路格式**: 
> 直接使用WebSocket（或SockJS）就很类似于使用TCP套接字来编写 Web应用。  
> 因为没有高层级的线路协议（wire protocol），因此就需 要我们定义应用之间所发送消息的语义，还需要确保连接的两端都能 遵循这些语义。  
> STOMP在 WebSocket之上提供了一个基于帧的线路格式（frame-based wire format）层，用来定义消息的语义。  

**STOMP帧**： 
> 在这个简单的样例中，STOMP命令是send，表明会发送一些内容。  
> 紧接着是两个头信息：一个用来表示消息要发送到哪里的目的地，另外一个则包含了负载的大小。  
> 然后，紧接着是一个空行，STOMP帧 的最后是负载内容，在本例中，是一个JSON消息。  
- STOMP帧由**命令**。  
- 一个或多个**头信息。   
- **以及**负载**所组成。  
```
SEND 
destination:/app/marco
content-length:20

{\"message\":\"Marco!\"}
```

**`destination`头消息表明STOMP是消息协议**： 
> STOMP帧中最有意思的恐怕就是destination头信息了。  
> 它表明 STOMP是一个消息协议，类似于JMS或AMQP。  
- 消息代理： 消息会发布到某个目的地，这个目的地实际上可能真的有消息代理（message broker）作为支撑。  
- 消息处理器： 另一方面，消息处理器（message handler）也可以监听这些目的地，接收所发送过来的消息。   

**WebSocket通信扩展了解**： 
> 在WebSocket通信中，基于浏览器的JavaScript应用可能会发送消息到 一个目的地，这个目的地由服务器端的组件来进行处理。  
> 其实，反过 来是一样的，服务器端的组件也可以发布消息，由JavaScript客户端 的目的地来接收。   

**Spring对STOMP的支持**： 
> Spring为STOMP消息提供了基于Spring MVC的编程模型。 
> 在Spring MVC控制器中处理STOMP消息与处理HTTP请求并没有 太大的差别。 
> 但首先，我们需要配置Spring启用基于STOMP的消息。  

#### 启用STOMP消息功能  

**`@MessageMapping`注解控制器方法处理STOMP消息**:  
> 何在Spring MVC中为控制器方法添 加@MessageMapping注解，使其处理STOMP消息，它与带 有@RequestMapping注解的方法处理HTTP请求的方式非常类似。   
> 但是与@RequestMapping不同的是，@MessageMapping的功能无 法通过@EnableWebMvc启用。  

**Spring的Web消息功能基于消息代理(message broker)**：  
> Spring的Web消息功能基于消息代理 （message broker）构建，因此除了告诉Spring我们想要处理消息以 外，还有其他的内容需要配置。我们必须要配置一个消息代理和其他 的一些消息目的地。 
> 因此除了告诉Spring我们想要处理消息以 外，还有其他的内容需要配置。  
> 我们必须要配置一个消息代理和其他 的一些消息目的地。  

**Stomp工作原理**： 
> 有三种消息目的地，通过消息目的地前缀来区分。
> 相应给客户端的消息目的地的前缀始终会替换为代理目的地的前缀。
![Stomp工作原理](Stomp工作原理.png)

**配置Spring Stomp**：  
- `@EnableWebSocketMessageBroker`注解： 这表明这个配置类 不仅配置了WebSocket，还配置了基于代理的STOMP消息。  
- `AbstractWebSocketMessageBrokerConfigurer`配置类：配置类需要继承该类。 
```java
@Configuration
@EnableWebSocketMessageBroker
public class ExampleAbstractWebSocketMessageBrokerConfigurer extends AbstractWebSocketMessageBrokerConfigurer {

    /**
     * 注册一个 STOMP 端点。
     * 客户端在订阅或发布消息到目的地路径前，要连接到该端点。
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/stomp").withSockJS();
    }

    /**
     * 如果不重载它的话，将会自动配置一个简单的内存消息代理，用它来处理以"/topic"为前缀的消息。
     * 当消息到达时，目的地前缀将会决定消息该如何处理。
     * 以应用程序为目的地的消息将会直接路由到带有@MessageMapping注解的控制其方法。
     * 发送到代理上的消息，将会路由到代理上。
     * {code @MessageMapping}注解方法的返回值形成的消息也会路由到代理上。
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //基于内存的STOMP消息代理，
        registry.enableSimpleBroker("/queue", "/topic");

        //基于RabbitMQ的STOMP消息代理
        //默认情况下，STOMP代理中继会假设代理监听localhost的61613端 口，并且客户端的username和password均为“guest”。
//        registry.//启用了 STOMP代理中继（broker relay）功能，并将其目的地前缀设置 为“/topic”和“/queue”。
//                enableStompBrokerRelay("/queue", "/topic")
//                .setRelayHost("localhost")
//                .setRelayPort(61613)
//                .setClientLogin("guest")
//                .setClientPasscode("guest");

        registry.setApplicationDestinationPrefixes("/app");//设置以应用程序为目的地的消息前缀。
        registry.setUserDestinationPrefix("/user");
    }
}
```

#### 处理来自客户端的STOMP消息  
```java
@Controller
public class ExampleStompController {

    /** 通过AnnotationMethodMessageHandler接收消息
     *  即处理发往/app/message的消息
     */
    @MessageMapping("/message")
    public void handle(Shout incoming){
        System.out.println(incoming.getMessage());
    }

    /**
     * 当收到STOMP订阅消息的时候，带有@SubscribeMapping注 解的方法将会触发。
     * 通过AnnotationMethodMessageHandler接收消息。
     * {code @SubscribeMapping}的主要应用场景是实现请求-回应模式。在请求-回应模式中，客户端订阅某一个目的地，然后预期在这个目的地 上获得一个一次性的响应。
     * 这里的关键区别在于 HTTP GET请求是同步的，而订阅的请求-回应模式则是异步的，这样 客户端能够在回应可用时再去处理，而不必等待。
     * @return
     */
    @SubscribeMapping("/subscribe")
    public Shout subscribe(){
        Shout shout = new Shout();
        shout.setMessage("订阅了一个消息");
        return shout;
    }
}
```

**编写JavaScript客户端**： 
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Send Stomp</title>
</head>
<body>
<script src="https://cdn.bootcdn.net/ajax/libs/sockjs-client/1.5.1/sockjs.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
    <script>
        var url = "http://" + window.location.host + "/learn-springMVC/stomp_endpoints";
        var sock = new SockJS(url);

        var stomp = Stomp.over(sock);   <!-- 创建STOMP客户端 -->

        var payload = JSON.stringify({'message': 'Marco!'});

        stomp.connect("guest", "guest", function(frame){
            <!-- 发送消息 -->
            stomp.send("/app/message", {}, payload);
            <!-- 订阅 subscribe(destination url, callback[, headers]) -->
            stomp.subscribe("/app/subscribe", function(message){
                var content = message.body;
                var obj = JSON.parse(content);
                console.log("订阅的服务端消息：" + obj.message);
            },{});
        });
    </script>

</body>
</html>
```


#### 发送消息到客户端  
Spring提供了两种发送数据给客户端的方法： 
- 作为处理消息或处理订阅的附带结果；  
- 使用消息模板。 
> 如果你想要在接收消息的时候，同时在响应中发送一条消息，那么需 要做的仅仅是将内容返回就可以了，方法签名不再是使用void。  
> SimpMessagingTemplate，它能够在应用的任何地方发送消息。 
> 使用SimpMessagingTemplate的最简单方式是将它（或者其接口 SimpMessage-SendingOperations）自动装配到所需的对象 中 
> 配置Spring支持STOMP的一个副作用就是在Spring应用上下文中已经 包含了SimpMessagingTemplate。因此，我们在这里没有必要再 创建新的实例。 
### 为目标用户发送消息  

### 处理消息异常  


