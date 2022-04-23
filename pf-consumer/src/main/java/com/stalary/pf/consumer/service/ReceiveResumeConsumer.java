package com.stalary.pf.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.stalary.pf.consumer.data.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.stalary.pf.consumer.data.constant.Constant.CONSUMER_GROUP;
import static com.stalary.pf.consumer.data.constant.Constant.RECEIVE_RESUME;

@Service
@Slf4j
@RocketMQMessageListener(consumerGroup = CONSUMER_GROUP, topic = RECEIVE_RESUME)
public class ReceiveResumeConsumer extends BaseConsumer {

    @Override
    public void onMessage(Map<String, String> messageMap) {
        String message = messageMap.get("value");

        SendResume resume = JSONObject.parseObject(message, SendResume.class);
        // 存储收到简历的消息通知(系统发送)
        Long recruitId = resume.getRecruitId();
        Long userId = resume.getUserId();
        Recruit recruit = recruitClient.getRecruit(recruitId).getData();
        UserInfo userInfo = userClient.getUserInfo(userId).getData();
        Long hrId = recruit.getHrId();
        Message m = new Message(0L, hrId, resume.getTitle() + "收到简历", resume.getTitle() + "收到来自" + userInfo.getSchool() + "的" + userInfo.getNickname() + "的简历", false);
        messageClient.saveMessage(m);
        outsideClient.sendEmail(new Email(userClient.getEmail(hrId).getData(), "收到投递简历", resume.getTitle() + "收到来自" + userInfo.getSchool() + "的" + userInfo.getNickname() + "的简历"));
        // 统计通知未读的数量
        int count = messageClient.getNotReadCount(hrId).getData();
        pushClient.sendMessage(hrId, "" + count);
    }
}
