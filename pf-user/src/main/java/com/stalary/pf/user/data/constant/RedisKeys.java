package com.stalary.pf.user.data.constant;

/**
 * RedisKeys
 *
 */
public class RedisKeys {

    /** token:user缓存 **/
    public static final String USER_TOKEN = "user_token";

    /** userId:user缓存 **/
    public static final String USER_ID = "user_id";

    /** 验证码缓存 **/
    public static final String VERIFY_CODE = "verify_code";

    /** 投递列表缓存 **/
    public static final String RESUME_SEND = "resume_send";

    /** 简历获取列表缓存 **/
    public static final String RESUME_RECEIVE = "resume_receive";
}