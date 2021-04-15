package pers.mortal.learn.springdata.orm.mybatis;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pers.mortal.learn.springdata.NumberType;

import java.util.HashMap;
import java.util.Map;

@Transactional
public class NumberTypeDAO {

    private SqlSessionTemplate sqlSessionTemplate;

    private NumberTypeMapper numberTypeMapper;

    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate){
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Autowired
    public void setNumberTypeMapper(NumberTypeMapper numberTypeMapper){
        this.numberTypeMapper = numberTypeMapper;
    }

    public Map<Integer, NumberType> getMap(){
        int count = sqlSessionTemplate.selectOne("SELECT Count(*) FROM NumberType");

        HashMap<Integer, NumberType> hashMap = new HashMap<>();

        for(int i=1; i<=count; i++){
            NumberType numberType = numberTypeMapper.selectNumber(i);
            hashMap.put(i, numberType);
        }

        return hashMap;
    }
}
