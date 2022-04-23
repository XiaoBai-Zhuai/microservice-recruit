package com.stalary.pf.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.stalary.pf.consumer.client.*;
import com.stalary.pf.consumer.data.dto.SendResume;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.stalary.pf.consumer.data.constant.Constant.CONSUMER_GROUP;
import static com.stalary.pf.consumer.data.constant.Constant.HANDLE_RESUME;

@Service
@Slf4j
@RocketMQMessageListener(consumerGroup = CONSUMER_GROUP, topic = HANDLE_RESUME)
public class HandleResumeConsumer extends BaseConsumer {

    @Override
    public void onMessage(Map<String, String> messageMap) {
        String message = messageMap.get("value");

        SendResume resume = JSONObject.parseObject(message, SendResume.class);
        // 处理投递简历
        resumeClient.handleResume(resume);
    }
}
