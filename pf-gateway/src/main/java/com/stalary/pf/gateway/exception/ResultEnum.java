package com.stalary.pf.gateway.exception;

import lombok.Getter;


public enum ResultEnum {
    UNKNOW_ERROR(500, "服务器错误"),

    // 1开头为用户有关的错误
    NEED_LOGIN(1001, "未登陆"),

    SUCCESS(0, "成功");

    @Getter
    private Integer code;

    @Getter
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
