package com.feng.shardingspheretest.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SqlTestMapper {

    @Select("${sql}")
    List<Map<String, Object>> getListBysql(String sql);

}
