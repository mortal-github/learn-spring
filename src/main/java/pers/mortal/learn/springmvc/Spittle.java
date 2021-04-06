package pers.mortal.learn.springmvc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Spittle {
    private final Long id;
    private final String message;
    private final Date time;
    private Double latitude;
    private Double longitude;

    public Spittle(String message, Date time){
        this(message, time, null, null);
    }

    public Spittle(String message, Date time, Double longitude, Double latitude){
        this.id = null;
        this.message = message;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getId(){
        return this.id;
    }

    public String getMessage(){
        return this.message;
    }

    public Date getTime(){
        return this.time;
    }

    public Double getLongitude(){
        return this.longitude;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    //就大部分内容来看，Spittle就是一个基本的POJO数据对象——没 有什么复杂的。
    // 唯一要注意的是，我们使用Apache Common Lang包来 实现equals()和hashCode()方法。
    // 这些方法除了常规的作用以 外，当我们为控制器的处理器方法编写测试时，它们也是有用的。
    @Override
    public boolean equals(Object other){
        return EqualsBuilder.reflectionEquals(this, other, "id", "time");
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this, "id", "time");
    }
}

