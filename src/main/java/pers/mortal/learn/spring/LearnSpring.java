package pers.mortal.learn.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class LearnSpring
{
    private static final String USER_PATH = System.getProperty("user.home");
    private static final String CURRENT_PATH = System.getProperty("user.dir");
    private static final String CLASS_PATH = LearnSpring.class.getResource("/").getPath();

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" + CLASS_PATH);
        useClassPathXmlApplicationContext();
        useAOP();
        useFileSystemXmlApplicationContext();
        useAnnotationConfigApplication();
    }

    public static void useClassPathXmlApplicationContext(){
       //加载上下文
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "META-INF/knight.xml");
        //获取knight bean
        Knight knight = context.getBean(Knight.class);
        //使用knight
        knight.embarkOnQuest();

        context.close();
    }

    public static void useFileSystemXmlApplicationContext(){
        //从文件系统下一个或多个XML配置问价加载上下文定义
        FileSystemXmlApplicationContext context =
                new FileSystemXmlApplicationContext(CLASS_PATH + "META-INF/knight.xml");
        //获取bean
        Knight knight = context.getBean(Knight.class);
        //使用bean
        knight.embarkOnQuest();
        context.close();
    }

    public static void useAnnotationConfigApplication(){
        //从一个或多个java配置类加载spring应用上下文。
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(KnightConfig.class);
        //获取bean
        Knight knight = context.getBean(Knight.class);
        //使用bean
        knight.embarkOnQuest();
        context.close();
    }

    public static void useAOP(){
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "META-INF/minstrel.xml");
        Knight knight = context.getBean(Knight.class);
        knight.embarkOnQuest();
        context.close();
    }
}

