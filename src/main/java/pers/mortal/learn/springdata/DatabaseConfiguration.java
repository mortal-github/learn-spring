package pers.mortal.learn.springdata;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackageClasses = {DatabaseConfiguration.class})
public class DatabaseConfiguration {
    public static String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
    public static String JDBC_URL = "jdbc:mysql://localhost:3306/example_database?serverTimezone=UTC";
    public static String JDBC_USERNAME = "root";
    public static String JDBC_PASSWORD = "root";

    @Bean
    public DataSource dataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(JDBC_USERNAME);
        config.setPassword(JDBC_PASSWORD);

        config.addDataSourceProperty("connectionTimeout", "1000");  //超时超时：1秒。
        config.addDataSourceProperty("idleTimeout", "60000");       //空闲超时：60秒。
        config.addDataSourceProperty("maximunPoolSize", "10");      //最大连接数：10。

        DataSource dataSource = new HikariDataSource(config);
        System.out.println("");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }

}

