package cc.sitec.kafka.example.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "testListener", topics = "canal_topic")
public class TestListener {
    public static final Logger logger = LoggerFactory.getLogger(TestListener.class);
    @KafkaHandler
    void listen(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        logger.info("table: {}", jsonObject.get("table"));
        logger.info("before: {}", jsonObject.get("old"));
        logger.info("after: {}", jsonObject.get("data"));
    }
}
