package com.stalary.pf.outside.client;

import com.stalary.pf.outside.data.ResponseMessage;
import com.stalary.pf.outside.data.UploadAvatar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@FeignClient(name = "pf-user")
@Component
public interface UserClient {

    @PostMapping(value = "/user/avatar", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    ResponseMessage uploadAvatar(@RequestBody UploadAvatar uploadAvatar);
}
