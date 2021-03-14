package com.jyrd.exam.base.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信参数对应实体类Vo
 */
@Component
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsPropertiesVo {
    private String host;
    private String path ;
    private String method;
    private String appcode;
    private String mobileNum;
    private String templateID;
    private String verfyCode;
    private Integer timeOut; // 单位：分钟
}
