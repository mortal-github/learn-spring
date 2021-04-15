package pers.mortal.learn.springdata.orm.mybatis;

import org.springframework.transaction.annotation.Transactional;
import pers.mortal.learn.springdata.NumberType;

import java.util.ArrayList;

@Transactional
public class NumberTypeService {

    private NumberTypeMapper numberTypeMapper;

    public NumberTypeService(NumberTypeMapper numberTypeMapper){
        this.numberTypeMapper = numberTypeMapper;
    }

    public ArrayList<NumberType> select(int min, int max){
        ArrayList<NumberType> list = new ArrayList<>();
        for(int i=min; i<= max; i++){
            NumberType numberType = this.numberTypeMapper.selectNumber(i);
            if(numberType != null){
                list.add(numberType);
            }
        }
        return list;
    }

    public int insert(int count, NumberType numberType){
        int real = 0;
        for(int i=0; i<count; i++){
            real += this.numberTypeMapper.insertNumber(numberType);
        }
        return real;
    }
}
