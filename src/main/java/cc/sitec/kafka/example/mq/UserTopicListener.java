package cc.sitec.kafka.example.mq;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserTopicListener extends DefaultTopicListener{
    @KafkaListener(topics = "canal_tsdb.t_user")
    public void handlerUser(String message){
        printMq(message);
    }
    @KafkaListener(topics = "canal_tsdb.t_role")
    public void handlerRole(String message){
        printMq(message);
    }
    @KafkaListener(topics = "canal_tsdb.t_user_role_mid")
    public void handlerUserRole(String message){
        printMq(message);
    }
}
