package pers.mortal.learn.spring.wiringbean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pers.mortal.learn.spring.wiringbean.javaconfig.CDPlayerConfig;

public class WiringBean {
    public static void main(String[] args){
        ApplicationContext context = getContextFromXML();

    }

    public static ClassPathXmlApplicationContext getContextFromXML(){
        return new ClassPathXmlApplicationContext("/META-INF/wiring_bean/wiring_bean.xml");
    }
    public static AnnotationConfigApplicationContext getContextFromAnnotation(){
        return new AnnotationConfigApplicationContext(CDPlayerConfig.class);
    }
}
