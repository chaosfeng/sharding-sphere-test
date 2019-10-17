# sharding sphere基本功能验证

## sharding sphere验证(4.0.0-RC2)
- sharding-jdbc(已完成)
    - 分库分表
    - 读写分离 + 分库分表
    - 编排治理之配置中心
    - 分布式事务之saga
    - 分库分表后SQL测试
- sharding-proxy(取消)
    
# saga流行方案验证(取消) 

# 运行/测试说明：
- 请先使用项目中的InitDatabase.sql创建相应的库和表
- 由于Saga事务相关依赖未发布至中央仓库，请将Saga所需依赖和测试类注释掉。
- 或者自行在本机上[打包部署Saga所需依赖](https://github.com/OpenSharding/shardingsphere-spi-impl)，参考[官方说明](https://shardingsphere.apache.org/document/current/cn/manual/sharding-jdbc/usage/transaction/#base-柔性-事务)