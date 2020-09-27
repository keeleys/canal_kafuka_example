### canal1.1.4 + kafka 

> java接收端

### 安装步骤
参考 https://github.com/alibaba/canal/wiki/QuickStart

依次安装mysql，zookeeper,kafka,配置启动canal

### run

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

