### canal1.1.4 + kafka 

> java接收端

### 安装步骤
参考 https://github.com/alibaba/canal/wiki/QuickStart

依次安装mysql，zookeeper,kafka,配置启动canal
### canal关键配置

1. instance.properties
```properties
# table meta tsdb info 
# 需要给canal授权canal_tsdb的curd
canal.instance.tsdb.enable=true
canal.instance.tsdb.url=jdbc:mysql://127.0.0.1:3306/canal_tsdb
canal.instance.tsdb.dbUsername=canal
canal.instance.tsdb.dbPassword=canal

# 数据库相关
canal.instance.master.address=127.0.0.1:3306
canal.instance.dbUsername=canal
canal.instance.dbPassword=canal

# table regex
canal.instance.filter.regex=.*\\..*
# table black regex
canal.instance.filter.black.regex=


# mq config
canal.mq.topic=canal_default_topic
# dynamic topic route by schema or table regex
# 这里是说针对canal_tsdb库的所有表，指定topic为 canal_tsdb.表名
canal.mq.dynamicTopic=canal_tsdb\\..*
canal.mq.partition=0


# hash partition config 
# kafka的分区数量配置和分区hash规则,下面的例子是按表名hash
# canal.mq.partitionsNum=3
# canal.mq.partitionHash=.*\\..*

```

2. canal.properties
```properties

canal.mq.servers=127.0.0.1:9092
canal.instance.tsdb.spring.xml = classpath:spring/tsdb/mysql-tsdb.xml

```

### 启动canal

1. 启动zk和kafka
    ```
    zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
    ```

2. 启动canal
    ```
    # 进入canal目录
    ./bin/startup.sh
    ```

3. 启动项目监听，然后修改数据记录看效果
    ```
    spring-boot:run
    ```

### QA

1. 关于mysql8的canal账户,如果出现`caching_sha2_password Auth failed`问题，
```sql
-- 执行下面的解决
ALTER USER 'canal'@'%' IDENTIFIED BY 'canal' PASSWORD EXPIRE NEVER;
ALTER USER 'canal'@'%' IDENTIFIED WITH mysql_native_password BY 'canal';
FLUSH PRIVILEGES;
```

