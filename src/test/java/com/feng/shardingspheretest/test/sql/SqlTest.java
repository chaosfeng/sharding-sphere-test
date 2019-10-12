package com.feng.shardingspheretest.test.sql;

import com.feng.shardingspheretest.domain.Order;
import com.feng.shardingspheretest.mapper.SqlTestMapper;
import com.feng.shardingspheretest.service.JDBCService;
import com.feng.shardingspheretest.service.OrderService;
import com.feng.shardingspheretest.service.UserService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * 分库分表,sql读写测试
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("inlineSharding")
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按方法名称排序执行
public class SqlTest {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    SqlTestMapper sqlTestMapper;

    @BeforeClass
    public static void init() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
        JDBCService.clearUserInAllDB();
    }

    @Test
    public void test1_initDataForTest() {
        // 插入用户，共4个表，每个表2条数据
        //master0.t_user_0
        userService.add(0, 0, "user00");
        userService.add(4, 2, "user42");
        //master0.t_user_1
        userService.add(2, 1, "user21");
        userService.add(6, 3, "user63");
        //master1.t_user_0
        userService.add(1, 0, "user10");
        userService.add(5, 2, "user52");
        //master1.t_user_1
        userService.add(3, 1, "user31");
        userService.add(7, 3, "user73");

        // 插入订单，共4个表，每个表4条数据
        for (int i = 0; i < 8; i++) {
            orderService.add(i, i);
            orderService.add(i + 9, i);
        }
        System.out.println("1");
    }

    @Test
    public void test2_order() {
        List<Order> list = orderService.getListBysql("SELECT * FROM t_order ORDER BY order_id DESC");
        for (int i = 0; i < list.size() - 1; i++) {
            String now = list.get(i).getOrderId();
            String next = list.get(i + 1).getOrderId();
            Assert.assertTrue(now.compareTo(next) > 0);
        }

        List<Order> list2 = orderService.getListBysql("SELECT * FROM t_order ORDER BY user_id ASC");
        for (int i = 0; i < list2.size() - 1; i++) {
            int now = list2.get(i).getUserId();
            int next = list2.get(i + 1).getUserId();
            Assert.assertTrue(now <= next);
        }
    }

    @Test
    public void test3_groupBy() {
        String sql = "SELECT user_id, COUNT(order_id) countOrder FROM t_order WHERE user_id in (0,1,2) AND order_id<10 GROUP BY user_id ORDER BY user_id ASC";
        List<Map<String, Object>> list = sqlTestMapper.getListBysql(sql);
        Assert.assertEquals(2L, list.get(0).get("countOrder"));
        Assert.assertEquals(1L, list.get(1).get("countOrder"));
        Assert.assertEquals(1L, list.get(2).get("countOrder"));

        String sql2 = "SELECT user_code, COUNT(1) count FROM t_user WHERE user_id <= 6 GROUP BY user_code ORDER BY user_code ASC";
        List<Map<String, Object>> list2 = sqlTestMapper.getListBysql(sql2);// 3位1
        Assert.assertEquals(2L, list2.get(0).get("count"));
        Assert.assertEquals(2L, list2.get(1).get("count"));
        Assert.assertEquals(2L, list2.get(2).get("count"));
        Assert.assertEquals(1L, list2.get(3).get("count"));
    }

    @Test
    public void test4_distinct() {
        String sql = "SELECT DISTINCT(user_code) code FROM t_user WHERE user_id in (0,1,2) ORDER BY user_code";
        List<Map<String, Object>> list = sqlTestMapper.getListBysql(sql);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(0, list.get(0).get("code"));
        Assert.assertEquals(1, list.get(1).get("code"));
    }

    @Test
    public void test5_limit() {
        // 实际sql如下：
        // Actual SQL: master0 ::: SELECT order_id FROM t_order_0 ORDER BY order_id LIMIT 0,16
        // Actual SQL: master0 ::: SELECT order_id FROM t_order_1 ORDER BY order_id LIMIT 0,16
        // Actual SQL: master1 ::: SELECT order_id FROM t_order_0 ORDER BY order_id LIMIT 0,16
        // Actual SQL: master1 ::: SELECT order_id FROM t_order_1 ORDER BY order_id LIMIT 0,16

        // 由于order_id定义为了varchar，此处转换为了数字进行排序
        String sql = "SELECT order_id FROM t_order ORDER BY CONVERT(order_id,SIGNED) DESC LIMIT 8,8";
        List<Map<String, Object>> list = sqlTestMapper.getListBysql(sql);
        Assert.assertEquals("7", list.get(0).get("order_id"));
        Assert.assertEquals("6", list.get(1).get("order_id"));
        Assert.assertEquals("5", list.get(2).get("order_id"));
        Assert.assertEquals("4", list.get(3).get("order_id"));
        Assert.assertEquals("3", list.get(4).get("order_id"));
        Assert.assertEquals("2", list.get(5).get("order_id"));
        Assert.assertEquals("1", list.get(6).get("order_id"));
        Assert.assertEquals("0", list.get(7).get("order_id"));
        System.out.println("1");
    }

    @Test
    public void test9_join() {
        // join不支持跨库，只会在库内进行join
        
        // bug here
        // join相关的A、B表，如果没有路由至单节点，则最终结果与期望可能有区别！！！！！！！！！！！！！！！
        // 实际sql如下：
        // Actual SQL: master0 ::: SELECT count(1) count FROM t_user_1 u INNER JOIN t_order_0 o ON u.user_id = o.user_id
        // Actual SQL: master0 ::: SELECT count(1) count FROM t_user_1 u INNER JOIN t_order_1 o ON u.user_id = o.user_id
        // Actual SQL: master0 ::: SELECT count(1) count FROM t_user_0 u INNER JOIN t_order_0 o ON u.user_id = o.user_id
        // Actual SQL: master0 ::: SELECT count(1) count FROM t_user_0 u INNER JOIN t_order_1 o ON u.user_id = o.user_id
        // Actual SQL: master1 ::: SELECT count(1) count FROM t_user_1 u INNER JOIN t_order_0 o ON u.user_id = o.user_id
        // Actual SQL: master1 ::: SELECT count(1) count FROM t_user_1 u INNER JOIN t_order_1 o ON u.user_id = o.user_id
        // Actual SQL: master1 ::: SELECT count(1) count FROM t_user_0 u INNER JOIN t_order_0 o ON u.user_id = o.user_id
        // Actual SQL: master1 ::: SELECT count(1) count FROM t_user_0 u INNER JOIN t_order_1 o ON u.user_id = o.user_id
        orderService.add(100, 100);
        userService.add(50,50,"test");
        // 错误
        String innerSql = "SELECT count(1) count FROM t_user u INNER JOIN t_order o ON u.user_id = o.user_id";
        List<Map<String, Object>> list = sqlTestMapper.getListBysql(innerSql);
        Assert.assertEquals(8L, list.get(0).get("count"));// 实际为16

        // 错误
        String leftJoinSql = "SELECT count(1) count FROM t_user u LEFT JOIN t_order o ON u.user_id = o.user_id";
        List<Map<String, Object>> list2 = sqlTestMapper.getListBysql(leftJoinSql);
        Assert.assertEquals(9L, list2.get(0).get("count"));// 实际为18

        // 错误
        String rightJoinSql = "SELECT count(1) count FROM t_user u RIGHT JOIN t_order o ON u.user_id = o.user_id";
        List<Map<String, Object>> list3 = sqlTestMapper.getListBysql(rightJoinSql);
        Assert.assertEquals(17L, list3.get(0).get("count"));// 实际为34
    }

    @AfterClass
    public static void clear() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
        JDBCService.clearUserInAllDB();
    }
}
