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
    List<Order> getByOrderId(int orderId);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE user_id = #{id}"
    })
    List<Order> getByUserId(int userId);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE order_id = #{orderId} and user_id = #{userId} "
    })
    List<Order> getByUserIdAndOrderId(@Param("orderId")int orderId, @Param("userId")int userId);

    @Insert({
            "INSERT INTO t_order (order_id, user_id)",
            "VALUES (#{order_id}, #{user_id})"
    })
    int add(@Param("order_id") int orderID, @Param("user_id")int userId);

}
