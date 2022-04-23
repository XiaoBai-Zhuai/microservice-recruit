package com.stalary.pf.consumer.service;

import com.stalary.pf.consumer.client.*;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BaseConsumer implements RocketMQListener<Map<String, String>> {

    public static ResumeClient resumeClient;

    @Autowired
    public void setResumeClient(ResumeClient resumeClient) {
        BaseConsumer.resumeClient = resumeClient;
    }

    public static PushClient pushClient;

    @Autowired
    public void setPushClient(PushClient pushClient) {
        BaseConsumer.pushClient = pushClient;
    }

    public static MessageClient messageClient;

    @Autowired
    public void setMessageClient(MessageClient messageClient) {
        BaseConsumer.messageClient = messageClient;
    }

    public static RecruitClient recruitClient;

    @Autowired
    public void setRecruitClient(RecruitClient recruitClient) {
        BaseConsumer.recruitClient = recruitClient;
    }

    public static UserClient userClient;

    @Autowired
    public void setUserClient(UserClient userClient) {
        BaseConsumer.userClient = userClient;
    }

    public static OutsideClient outsideClient;

    @Autowired
    public void setOutsideClient(OutsideClient outsideClient) {
        BaseConsumer.outsideClient = outsideClient;
    }

    @Override
    public void onMessage(Map<String, String> messageMap) {

    }
}
