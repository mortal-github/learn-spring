package pers.mortal.learn.springdata.orm.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pers.mortal.learn.springdata.DatabaseConfiguration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackageClasses = {SpringMybatisConfiguration.class})
@Import(value = {DatabaseConfiguration.class})
@EnableTransactionManagement
public class SpringMybatisConfiguration {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //必备属性dataSource
        sqlSessionFactoryBean.setDataSource(dataSource);
        //可选属性
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("META-INF/mybatis/config.xml"));  //加载Mybatis的XML配置文件。
        sqlSessionFactoryBean.setMapperLocations(new Resource[] {new ClassPathResource("META-INF/mybatis/mapper.xml")}); //加载Mybatis的Mapper映射文件（若配置文件有对应的<mapper>元素则不必再加载mapper文件）。
        //configuration属性配置Configuration配置Setting
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.setMapUnderscoreToCamelCase(true);
//        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public NumberTypeMapper numberTypeMapper(SqlSessionTemplate sqlSessionTemplate){
        return sqlSessionTemplate.getMapper(NumberTypeMapper.class);
    }

    @Bean
    public NumberTypeService numberTypeService(NumberTypeMapper numberTypeMapper){
        return new NumberTypeService(numberTypeMapper);
    }

    //开启Spring的事务处理功能
    //一个使用 MyBatis-Spring 的其中一个主要原因是它允许 MyBatis 参与到 Spring 的事务管理中。
    //MyBatis-Spring 借助了 Spring 中的 DataSourceTransactionManager 来实现事务管理。
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);//dataSource必须和用来创建sqlSessionFactoryBean是相同的一个数据源。
    }

    //交由容器管理事务。
    //如果你正使用一个 JEE 容器而且想让 Spring 参与到容器管理事务（Container managed transactions，CMT）的过程中，
    //那么 Spring 应该被设置为使用 JtaTransactionManager 或由容器指定的一个子类作为事务管理器。
    //最简单的方式是使用 Spring 的事务命名空间或使用 JtaTransactionManagerFactoryBean：
    //<tx:jta-transaction-manager />
    //注意，如果你想使用由容器管理的事务，而不想使用 Spring 的事务管理，你就不能配置任何的 Spring 事务管理器。
    //并必须配置 SqlSessionFactoryBean 以使用基本的 MyBatis 的 ManagedTransactionFactory：
//    @Bean
//    public JtaTransactionManager transactionManager(){
//        return new JtaTransactionManagerFactoryBean().getObject();
//    }

}
