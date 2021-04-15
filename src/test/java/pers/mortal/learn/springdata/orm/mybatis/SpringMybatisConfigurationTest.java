package pers.mortal.learn.springdata.orm.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.mortal.learn.springdata.NumberType;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringMybatisConfiguration.class})
public class SpringMybatisConfigurationTest {

    @Autowired(required = true)
    private SqlSessionFactory  sqlSessionFactory;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired(required = true)
    private NumberTypeMapper numberTypeMapper;

    @Autowired
    private NumberTypeService numberTypeService;

    @Test
    public void sqlSessionFactory() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        NumberType numberType = sqlSession.selectOne(
                "pers.mortal.learn.springdata.orm.mybatis.NumberTypeMapper.selectNumber", 1);
        numberType.setColumn8(-9999.99999F);
        int column = sqlSession.insert("pers.mortal.learn.springdata.orm.mybatis.NumberTypeMapper.insertNumber", numberType);
        assert column == 1;
    }

    @Test
    public void numberTypeMapper() {
        NumberType numberType = numberTypeMapper.selectNumber(2);
        numberTypeMapper.insertNumber(numberType);
    }

    @Test
    public void testDataSourceTransactionManager(){
        ArrayList<NumberType> list = numberTypeService.select(1, 20);
        int count = numberTypeService.insert(10, list.get(2));
    }
}