package com.stalary.pf.consumer.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class ConsumerAop {

    @Pointcut("execution(void com.stalary.pf.consumer.service.BaseConsumer.onMessage(..))")
    public void consumer() {

    }

    @SuppressWarnings("unchecked")
    @Around("consumer()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object[] args = proceedingJoinPoint.getArgs();//得到方法执行所需的参数
            Map<String, String> messageMap = (Map<String, String>) args[0];
            long startTime = System.currentTimeMillis();
            String message = messageMap.get("value");
            log.info("receive message: "+ message);

            Object rtValue = proceedingJoinPoint.proceed(args);//明确调用切入点方法（切入点方法）

            long endTime = System.currentTimeMillis();
            log.info("Consumer.time=" + (endTime - startTime));

            return rtValue;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
