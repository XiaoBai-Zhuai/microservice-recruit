package com.stalary.pf.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.stalary.pf.consumer.client.*;
import com.stalary.pf.consumer.data.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.stalary.pf.consumer.data.constant.Constant.*;

@Service
@Slf4j
@RocketMQMessageListener(consumerGroup = CONSUMER_GROUP, topic = SEND_RESUME)
public class SendResumeConsumer extends BaseConsumer {

    @Override
    public void onMessage(Map<String, String > messageMap) {
        String message = messageMap.get("value");
        SendResume resume = JSONObject.parseObject(message, SendResume.class);
        // 存储投递的消息通知(系统发送)
        Long userId = resume.getUserId();
        Message m = new Message(0L, userId, "简历投递成功", resume.getTitle() + "简历投递成功", false);
        messageClient.saveMessage(m);
        // 统计通知未读的数量
        int count = messageClient.getNotReadCount(userId).getData();
        pushClient.sendMessage(userId, "" + count);
    }

}