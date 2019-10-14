package com.feng.shardingjdbctest.mapper;

import com.feng.shardingjdbctest.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("${sql}")
    List<User> getListBysql(String sql);

    @Select({
            "SELECT *",
            "FROM t_user",
            "WHERE user_id = #{orderId}"
    })
    List<User> getByUserId(int userId);

    @Insert({
            "INSERT INTO t_user (user_id, user_code, user_name)",
            "VALUES (#{user_id}, #{user_code}, #{user_name})"
    })
    int add(@Param("user_id") int userId, @Param("user_code") int userCode, @Param("user_name") String userName);

    @Select({
            "SELECT *",
            "FROM t_user",
    })
    List<User> getAll();

}
