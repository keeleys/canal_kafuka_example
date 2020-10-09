## mysql8/zookeeper/kafka/canal1.1.4

![](https://user-gold-cdn.xitu.io/2020/3/17/170e8dbf385d294f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
![img_master](https://camo.githubusercontent.com/eec1605862fe9e9989b97dd24f28a4bc5d7debec/687474703a2f2f646c2e69746579652e636f6d2f75706c6f61642f6174746163686d656e742f303038302f333038362f34363863316131342d653761642d333239302d396433642d3434616335303161373232372e6a7067)


## 安装步骤
参考 https://github.com/alibaba/canal/wiki/QuickStart
依配置启动mysql，zookeeper,kafka, 启动canal

### 配置mysql (vim /usr/local/etc/my.cnf)
```
# Default Homebrew MySQL server config
[mysqld]
# Only allow connections from localhost
bind-address = 0.0.0.0
mysqlx-bind-address = 0.0.0.0

# 开启 binlog
log-bin=mysql-bin
# 选择 ROW 模式
binlog-format=ROW 
# 配置 MySQL replaction 需要定义，不要和 canal 的 slaveId 重复
server_id=1
```

### [安装zookeeper和kafka](https://kafka.apache.org/quickstart)

```
## 启动
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
```

### [安装canal](https://github.com/alibaba/canal/wiki/QuickStart)
docker 启动

```
# docker 启动
docker run --name canal-server \
-e canal.instance.master.address=172.16.101.72:3306 \
-e canal.instance.dbUsername=canal \
-e canal.instance.dbPassword=canal \
-e canal.mq.topic=canal_default_topic \
-e canal.serverMode=kafka \
-e canal.mq.dynamicTopic="canal_tsdb\\..*" \
-e canal.mq.servers=172.16.101.72:9092 \
-p 11111:11111 \
-d canal/canal-server:v1.1.4

# 查看日志
docker exec -it canal-server bash
tail -f /home/admin/canal-server/logs/canal/canal.log
tail -f /home/admin/canal-server/logs/example/example.log
tail -f /home/admin/canal-server/logs/example/meta.log
```
本地启动
```
# 下载 https://github.com/alibaba/canal/releases/download/canal-1.1.4/canal.deployer-1.1.4.tar.gz

# 修改conf/example/instance.properties和conf/canal.properties
# 本地启动 进入canal目录

./bin/startup.sh

tail -f logs/canal/canal.log
tail -f logs/example/example.log

./bin/stop.sh
```

### 启动项目监听，然后修改数据记录看效果
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
```


### 运行日志

```txt
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
