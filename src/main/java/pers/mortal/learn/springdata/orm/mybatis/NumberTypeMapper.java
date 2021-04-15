package pers.mortal.learn.springdata.orm.mybatis;

import pers.mortal.learn.springdata.NumberType;

public interface NumberTypeMapper {
//    @Select("SELECT * FROM NumberType WHERE id = #{id}")
//    NumberType selectNumber(@Param("id") int id);

    NumberType selectNumber(int id);

    int insertNumber(NumberType numberType);
}
