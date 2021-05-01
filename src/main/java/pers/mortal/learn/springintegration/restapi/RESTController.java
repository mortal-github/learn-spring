package pers.mortal.learn.springintegration.restapi;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.IntStream;

@Controller
public class RESTController {
    private List<Integer> list = new ArrayList<>();
    {
        for(int i = 0; i < 10; i++){
            list.add(i);
        }
    }

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

    //@ResponseBody将方法结果作为消息体转化为客户端合适的类型。
    @RequestMapping(value = "/rest/response_body", method = RequestMethod.GET
            ,produces = "application/json"
    )
    public @ResponseBody
    List<Integer> useProduces(){
        return list;
    }

    //@RequestBody将客户端消息转化为参数对象。
    @RequestMapping(value = "/rest/request_body", method = RequestMethod.POST
            , produces = {"application/json"}
            , consumes = {"application/json"})
    public @ResponseBody List<Integer> useConsumes(@RequestBody List<Integer> list){
        return list;
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
    @RequestMapping(value = "/rest/exception_handle"
        ,produces = {"application/json"})
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

    @RequestMapping(
            value = "/rest/resource",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
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
    static{
        int count = 10;
        for(int i = 1; i <= count; i++){
            int[] values = IntStream.range(0, i).toArray();
            List<Integer> list = new ArrayList<>();
            for(int v : values){
                list.add(v);
            }

            save(list);
        }
    }

    public static int save(List<Integer> list){
        id++;
        map.put(id, list);
        return id;
    }

    public static List<Integer> get(int id){
        return map.get(id);
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