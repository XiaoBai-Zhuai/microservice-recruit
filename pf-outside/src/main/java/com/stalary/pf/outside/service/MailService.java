package com.stalary.pf.outside.service;

import com.stalary.pf.outside.data.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * MailService
 *
 * @author lirongqian
 * @since 2018/04/27
 */
@Service
@Slf4j
@RefreshScope
public class MailService {

    private static final String REGISTER_SUBJECT = "Leader直聘注册验证码";

    private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    private static final long EXPIRE_TIME = 10;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.password}")
    private String psw;

    @Value("${spring.mail.host}")
    private String host;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redis;

    public void sendEmail(String to, String subject, String message) {
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            //设置邮件会话参数
            Properties props = new Properties();
            //邮箱的发送服务器地址
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            //邮箱发送服务器端口,这里设置为465端口
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.auth", "true");
            final String username = from;
            final String password = psw;
            //获取到邮箱会话,利用匿名内部类的方式,将发送者邮箱用户名和密码授权给jvm
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            //通过会话,得到一个邮件,用于发送
            Message msg = new MimeMessage(session);
            //设置发件人
            msg.setFrom(new InternetAddress(username));
            //设置收件人,to为收件人,cc为抄送,bcc为密送
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            //设置邮件消息
            msg.setText(message);
            //设置发送的日期
            msg.setSentDate(new Date());
            //调用Transport的send方法去发送邮件
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送注册验证码
     * @param email
     * @param randomCode
     */
    public String sendVerCode(String email) {
        log.info("邮箱为: " + email);
        String randomCode = RandomStringUtils.randomNumeric(6);
        // 将验证码放到redis里
        redis.opsForValue().set(Constant.getKey(Constant.VERIFY_CODE, email), randomCode, EXPIRE_TIME, TimeUnit.MINUTES);
        String message = "欢迎注册leader直聘，您的验证码为：\n\ncode: " + randomCode;
        message += "\n\n验证码有效期为10分钟，请尽快完成注册!";
        // 发送邮件
        sendEmail(email, REGISTER_SUBJECT, message);
        return randomCode;
    }


}
