package com.stalary.pf.user.service;

import com.alibaba.fastjson.JSONObject;
import com.stalary.pf.user.client.UserCenterClient;
import com.stalary.pf.user.data.constant.Constant;
import com.stalary.pf.user.data.constant.RedisKeys;
import com.stalary.pf.user.data.dto.Applicant;
import com.stalary.pf.user.data.dto.HR;
import com.stalary.pf.user.data.dto.ProjectInfo;
import com.stalary.pf.user.data.dto.User;
import com.stalary.pf.user.data.vo.ResponseMessage;
import com.stalary.pf.user.exception.MyException;
import com.stalary.pf.user.exception.ResultEnum;
import com.stalary.pf.user.holder.IpHolder;
import com.stalary.pf.user.holder.UserHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * WebClient
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@Slf4j
@Service
public class ClientService {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redis;

    @Resource
    private UserCenterClient userCenterClient;

    @Value("${server.debug}")
    @Getter
    private boolean debug;

    private ProjectInfo projectInfo;

    public void genProjectInfo() {
        // 获取项目信息
        if (projectInfo == null) {
            ResponseMessage<ProjectInfo> info = userCenterClient.getProjectInfo("leader直聘", "18229809790");
            if (info.isSuccess()) {
                projectInfo = info.getData();
            } else {
                log.warn("genProjectInfo error");
            }
        }
    }

    public ResponseMessage postUser(Object object, String type) {
        User user;
        // 当登录注册时，判断对象类型
        if (object instanceof Applicant) {
            Applicant applicant = (Applicant) object;
            user = new User(applicant);
        } else if (object instanceof HR) {
            HR hr = (HR) object;
            user = new User(hr);
        } else {
            user = (User) object;
        }
        // 写入项目id，用于请求用户中心
        user.setProjectId(projectInfo.getProjectId());
        ResponseMessage<String> response = new ResponseMessage<>();
        switch (type) {
            case Constant.UPDATE_PASSWORD: { // 修改密码
                response = userCenterClient.updatePassword(projectInfo.getKey(), user);
                break;
            }
            case Constant.UPDATE: { // 修改用户信息
                response = userCenterClient.updateInfo(projectInfo.getKey(), user);
                break;
            }
            case Constant.REGISTER: { // 注册
                // 注册需要先校验短信验证码
                String code = redis.opsForValue().get(Constant.getKey(RedisKeys.VERIFY_CODE, user.getEmail()));
                if (StringUtils.isEmpty(code)) {
                    throw new MyException(ResultEnum.CODE_EXPIRE);
                }
                if (!code.equals(user.getCode())) {
                    throw new MyException(ResultEnum.CODE_ERROR);
                }
                response = userCenterClient.register(projectInfo.getKey(), user);
                break;
            }
            case Constant.LOGIN: { // 登录
                response = userCenterClient.login(projectInfo.getKey(), user);
                break;
            }
            default: {
                log.warn("postUser type " + type + " error");
            }
        }
        if (!response.isSuccess()) {
            throw new MyException(response.getCode(), response.getMsg());
        }
        return response;
    }

    /**
     * 获取到用户信息时即存入缓存
     *
     * @param token
     * @return
     */
    public User getUser(String token) {
        String redisKey = Constant.getKey(RedisKeys.USER_TOKEN, token);
        String redisData = redis.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(redisData)) {
            ResponseMessage<User> response = userCenterClient.getUserInfo(token, projectInfo.getKey());
            if (response.isSuccess()) {
                User user = response.getData();
                // token是临时的，所以只缓存一天
                // todo: 缓存失效应该重新登录
                redis.opsForValue().set(redisKey, JSONObject.toJSONString(user), 1, TimeUnit.DAYS);
                UserHolder.set(user);
                return user;
            } else {
                throw new MyException(response.getCode(), response.getMsg());
            }
        }
        User user = JSONObject.parseObject(redisData, User.class);
        UserHolder.set(user);
        return user;
    }

    /**
     * 通过id获取用户信息
     */
    public User getUser(Long userId) {
        if (projectInfo == null) {
            genProjectInfo();
        }
        String redisKey = Constant.getKey(RedisKeys.USER_ID, String.valueOf(userId));
        String redisData = redis.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(redisData)) {
            ResponseMessage<User> response = userCenterClient.getUserInfoById(userId, projectInfo.getKey(), projectInfo.getProjectId());
            if (response.isSuccess()) {
                User user = response.getData();
                // userId与User映射缓存7天
                redis.opsForValue().set(redisKey, JSONObject.toJSONString(user), 7, TimeUnit.DAYS);
                return response.getData();
            } else {
                throw new MyException(response.getCode(), response.getMsg());
            }
        }
        return JSONObject.parseObject(redisData, User.class);
    }

}