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
