package cc.sitec.kafka.example.mq;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultTopicListener extends AbstractTopicListener{
    @KafkaListener(topics = "canal_default_topic")
    void listen(String message) {
        this.printMq(message);
    }
}
