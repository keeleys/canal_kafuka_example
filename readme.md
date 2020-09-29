### canal1.1.4 + kafka 

![](https://user-gold-cdn.xitu.io/2020/3/17/170e8dbf385d294f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
![img_master](https://camo.githubusercontent.com/eec1605862fe9e9989b97dd24f28a4bc5d7debec/687474703a2f2f646c2e69746579652e636f6d2f75706c6f61642f6174746163686d656e742f303038302f333038362f34363863316131342d653761642d333239302d396433642d3434616335303161373232372e6a7067)

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

canal.serverMode = kafka
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

2. mysql 链接问题
```
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'xxxxxx';
``


### 运行日志

```
2020-09-27 18:05:16.535  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : message: {"data":[{"id":"2","account":"","user_name":"0","password":"","age":null,"create_time":"2020-09-27 18:05:16","update_time":"2020-09-27 18:05:16"}],"database":"canal_tsdb","es":1601201116000,"id":2,"isDdl":false,"mysqlType":{"id":"int unsigned","account":"varchar(50)","user_name":"int","password":"varchar(50)","age":"INT","create_time":"timestamp","update_time":"timestamp"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":4,"account":12,"user_name":4,"password":12,"age":4,"create_time":93,"update_time":93},"table":"t_user","ts":1601201116505,"type":"INSERT"}
2020-09-27 18:05:16.578  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : table: t_user
2020-09-27 18:05:16.578  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : before: null
2020-09-27 18:05:16.578  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : after: [{"password":"","update_time":"2020-09-27 18:05:16","create_time":"2020-09-27 18:05:16","user_name":"0","id":"2","account":""}]
2020-09-27 18:05:24.824  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : message: {"data":[{"id":"4","account":"222","user_name":"0","password":"","age":null,"create_time":"2020-09-27 18:05:24","update_time":"2020-09-27 18:05:24"}],"database":"canal_tsdb","es":1601201124000,"id":3,"isDdl":false,"mysqlType":{"id":"int unsigned","account":"varchar(50)","user_name":"int","password":"varchar(50)","age":"INT","create_time":"timestamp","update_time":"timestamp"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":4,"account":12,"user_name":4,"password":12,"age":4,"create_time":93,"update_time":93},"table":"t_user","ts":1601201124819,"type":"INSERT"}
2020-09-27 18:05:24.825  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : table: t_user
2020-09-27 18:05:24.825  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : before: null
2020-09-27 18:05:24.825  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : after: [{"password":"","update_time":"2020-09-27 18:05:24","create_time":"2020-09-27 18:05:24","user_name":"0","id":"4","account":"222"}]
2020-09-27 18:05:39.472  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : message: {"data":[{"id":"2","account":"","user_name":"0","password":"","age":null,"create_time":"2020-09-27 18:05:16","update_time":"2020-09-27 18:05:16"}],"database":"canal_tsdb","es":1601201139000,"id":4,"isDdl":false,"mysqlType":{"id":"int unsigned","account":"varchar(50)","user_name":"int","password":"varchar(50)","age":"INT","create_time":"timestamp","update_time":"timestamp"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":4,"account":12,"user_name":4,"password":12,"age":4,"create_time":93,"update_time":93},"table":"t_user","ts":1601201139467,"type":"DELETE"}
2020-09-27 18:05:39.472  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : table: t_user
2020-09-27 18:05:39.473  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : before: null
2020-09-27 18:05:39.473  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : after: [{"password":"","update_time":"2020-09-27 18:05:16","create_time":"2020-09-27 18:05:16","user_name":"0","id":"2","account":""}]
2020-09-27 18:13:21.357  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : message: {"data":[{"id":"4","account":"222","user_name":"1","password":"","age":null,"create_time":"2020-09-27 18:05:24","update_time":"2020-09-27 18:13:21"}],"database":"canal_tsdb","es":1601201601000,"id":5,"isDdl":false,"mysqlType":{"id":"int unsigned","account":"varchar(50)","user_name":"int","password":"varchar(50)","age":"INT","create_time":"timestamp","update_time":"timestamp"},"old":[{"user_name":"0","update_time":"2020-09-27 18:05:24"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"account":12,"user_name":4,"password":12,"age":4,"create_time":93,"update_time":93},"table":"t_user","ts":1601201601352,"type":"UPDATE"}
2020-09-27 18:13:21.358  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : table: t_user
2020-09-27 18:13:21.358  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : before: [{"update_time":"2020-09-27 18:05:24","user_name":"0"}]
2020-09-27 18:13:21.358  INFO 44222 --- [ntainer#3-0-C-1] c.s.kafka.example.mq.UserTopicListener   : after: [{"password":"","update_time":"2020-09-27 18:13:21","create_time":"2020-09-27 18:05:24","user_name":"1","id":"4","account":"222"}]

```