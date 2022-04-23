package com.stalary.pf.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "pf-message")
@Component
public interface MessageClient {

    @GetMapping("/message/sendCount")
    void sendCount(@RequestParam("userId") Long userId);
}
