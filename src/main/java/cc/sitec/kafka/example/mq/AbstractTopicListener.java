package cc.sitec.kafka.example.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTopicListener {
    public Logger logger = LoggerFactory.getLogger(this.getClass());
    protected void printMq(String message){
        logger.info("message: {}", message);
        if(message==null || message.isEmpty()) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(message);
        logger.info("table: {}", jsonObject.get("table"));
        logger.info("before: {}", jsonObject.get("old"));
        logger.info("after: {}", jsonObject.get("data"));
    }
}
