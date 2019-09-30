package com.feng.shardingspheretest.mapper;

import com.feng.shardingspheretest.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE order_id = #{id}"
    })
    List<Order> getById(int id);

    @Insert({
            "INSERT INTO t_order (order_id, user_id, description)",
            "VALUES (#{order_id}, #{user_id}, #{description})"
    })
    int add(@Param("order_id") int orderID, @Param("user_id")int userId, @Param("description")String description);

}
