package pers.mortal.learn.springdata.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExampleCache {
    public Map<Integer, CacheData> repository = new HashMap<>() ;

    @CachePut(
            value = "data",
            key = "#result.id",
            condition = "#data.condition",//必须要在进入方法时进行计算，所以我们不能通过 #result引用返回值。
            unless = "#result.unless"//为unless属性只有在缓存方法有返回值时才开始发挥作用。
    )
    public CacheData add(CacheData data){
        repository.put(data.getId(), data);
        return data;
    }

    //方法调用前取缓存，若无缓存则调用方法。
    @Cacheable(
            value = "data",
            key = "#id",
            unless = "#result == null || #result.unless"
    )
    public CacheData get(int id){
        return repository.get(id);
    }
    //移除缓存的场景在于缓存不合适的时候，如删除数据。
    @CacheEvict(
            value = "data",
            key = "#id",
            beforeInvocation = false,//默认为false
            allEntries = false//默认为false
    )
    public CacheData remove(int id){
       return repository.remove(id);
    }

    @CacheEvict(
            value = "data",
            allEntries = true
    )
    public void clear(){

    }

//    @Caching(
//            put = {@CachePut(value="data", key="#result.id")},
//            cacheable = {@Cacheable(value="data", key="#id")},
//            evict = {@CacheEvict(value = "data", key="#id")}
//    )
//    public CacheData all(int id){
//
//    }
}