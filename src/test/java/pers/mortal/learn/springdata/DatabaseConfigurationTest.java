package pers.mortal.learn.springdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
public class DatabaseConfigurationTest {

    @Autowired(required = true)
    DataSource dataSource;

    //通过注入JdbcOperations，而不是具体的JdbcTemplate，能够保证JdbcSpitterRepository通 过JdbcOperations接口达到与JdbcTemplate保持松耦合。
    @Autowired(required = true)
    JdbcOperations jdbcOperations;//。JdbcOperations是一个接口，定义了 JdbcTemplate所实现的操作。

    //注入NamedParameterJdbcOperations与注入JdbcOperations`同理。
    @Autowired(required = true)
    NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Test
    public void testDataSource(){
       try(Connection connection = dataSource.getConnection()){
           PreparedStatement statement = connection.prepareStatement("SELECT * FROM NumberType");
           ResultSet resultSet = statement.executeQuery();

          while(resultSet.next()){
            int id = resultSet.getInt("id");
            short column1 = resultSet.getShort("column1");
            int column2 = resultSet.getInt("column2");
            int column3 = resultSet.getInt("column3");
            int column4 = resultSet.getInt("column4");
            long column5 = resultSet.getLong("column5");
            BigDecimal column6 = resultSet.getBigDecimal("column6");
            BigDecimal column7 = resultSet.getBigDecimal("column7");
            double column8 = resultSet.getDouble("column8");
            double column9 = resultSet.getDouble("column9");
            byte column10 = resultSet.getByte("column10");
            byte[] columns = resultSet.getBytes("column10");

            System.out.println("");
          }

       }catch (SQLException e){
            e.printStackTrace();
       }
    }

    @Test
    public void testJdbcTemplate(){
        jdbcOperations.update("INSERT " +
                "INTO NumberType(column1,column2,column3,column4,column5,column6,column7,column8,column9, column10)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?)",
                1,2,3,4,8,15,15,32,64, 0b1001);
        Map<String, Object> map = jdbcOperations.queryForMap("SELECT * FROM NumberType WHERE id = ?", 1);
        for(Map.Entry<String, Object> entry : map.entrySet()){
            System.out.println(entry.getKey() + " = " + entry.getValue() + " [" + entry.getValue().getClass() + " ]");
        }
        System.out.println();
    }

    @Test
    public void testNamedParameterJdbcTemplate(){
        //使用命名参数
        final String INSERT_NUMBER = "INSERT " +
                "INTO NumberType(column1,column2,column3,column4,column5,column6,column7,column8,column9, column10)" +
                "VALUES ( :column1, :column2, :column3, :column4, :column5, :column6, :column7, :column8, :column9, :column10)";
        //绑定命名参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("column1", 1);
        paramMap.put("column2", 2);
        paramMap.put("column3", 3);
        paramMap.put("column4", 4);
        paramMap.put("column5", 8);
        paramMap.put("column6", 15);
        paramMap.put("column7", 15);
        paramMap.put("column8", 32);
        paramMap.put("column9", 64);
        paramMap.put("column10", 0b1111);

        //执行数据
        namedParameterJdbcOperations.update(INSERT_NUMBER, paramMap);
    }
}