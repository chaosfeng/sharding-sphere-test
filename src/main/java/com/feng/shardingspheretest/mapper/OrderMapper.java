package com.feng.shardingspheretest.mapper;

import com.feng.shardingspheretest.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("${sql}")
    List<Order> getListBysql(String sql);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE order_id = #{orderId}"
    })
    List<Order> getByOrderId(int orderId);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE user_id = #{orderId}"
    })
    List<Order> getByUserId(int userId);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE order_id = #{order_id} and user_id = #{userId}"
    })
    List<Order> getByUserIdAndOrderId(@Param("order_id") int orderId, @Param("userId") int userId);

    @Insert({
            "INSERT INTO t_order (order_id, user_id)",
            "VALUES (#{orderId}, #{userId})"
    })
    int add(@Param("orderId") int orderId, @Param("userId") int userId);

    @Select({
            "SELECT *",
            "FROM t_order",
    })
    List<Order> getAll();


    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE orderId BETWEEN #{orderIdStart} AND #{orderIdEnd}"
    })
    List<Order> getByRangeOrderId(@Param("orderIdStart") int orderIdStart, @Param("orderIdEnd") int orderIdEnd);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE user_id BETWEEN #{userIdStart} AND #{userIdEnd}"
    })
    List<Order> getByRangeUserId(@Param("userIdStart") int userIdStart, @Param("userIdEnd") int userIdEnd);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE orderId BETWEEN #{orderIdStart} and #{orderIdEnd}",
            "AND user_id BETWEEN #{userIdStart} and #{userIdEnd}",
    })
    List<Order> getByRangeOrderIdAndUserId(@Param("orderIdStart") int orderIdStart, @Param("orderIdEnd") int orderIdEnd,
                                           @Param("userIdStart") int userIdStart, @Param("userIdEnd") int userIdEnd);

}
