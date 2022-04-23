package com.stalary.pf.outside.service;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.stalary.pf.outside.exception.MyException;
import com.stalary.pf.outside.exception.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * QiNiuService
 *
 */
@Service
@Slf4j
public class QiNiuService {

    /**
     * TODO 配置放到nacos上去
     * 七牛云
     **/
    private static String ACCESS_KEY = "GoLY9xkGmfGqxunzyBfUlLPZuSh-g8z5JsdQGY7r";
    private static String SECRET_KEY = "n3RI7um0WqYhzW82Ijc8HOYspCqEVuzn1ib1diS3";
    private static String BUCKET_NAME = "m-recruit";
    private static String QINIU_IMAGE_DOMAIN = "http://rasklg6c3.hn-bkt.clouddn.com/";

    private Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    Configuration cfg = new Configuration(Zone.huanan());
    private UploadManager uploadManager = new UploadManager(cfg);

    private String getUpToken() {
        return auth.uploadToken(BUCKET_NAME);
    }

    public String uploadPicture(MultipartFile picture, String name) {
        try {
            Response response = uploadManager.put(picture.getBytes(), name, getUpToken());
            if (response.isOK() && response.isJson()) {
                return QINIU_IMAGE_DOMAIN + name;
            }
        } catch (Exception e) {
            log.warn("upload picture error", e);
            throw new MyException(ResultEnum.QINIU_ERROR);
        }
        return QINIU_IMAGE_DOMAIN + name;
    }
}