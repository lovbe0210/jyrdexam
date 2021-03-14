package com.jyrd.exam.base.common.utils;


import com.google.gson.Gson;
import com.jyrd.exam.base.vo.SmsPropertiesVo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送工具
 *
 * @author lovbe
 */
public class SMSUtils {

    public static Map<String, String> sendSmS(SmsPropertiesVo smsProperties) {
        String host = smsProperties.getHost();
        String path = smsProperties.getPath();
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + smsProperties.getAppcode());
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("callbackUrl", "http://test.dev.esandcloud.com");
        bodys.put("channel", "0");
        bodys.put("mobileSet", "[86"+smsProperties.getMobileNum()+"]");
        bodys.put("templateID", smsProperties.getTemplateID());
        bodys.put("templateParamSet",
                "["+smsProperties.getVerfyCode()+", "+smsProperties.getTimeOut()+"]");
        Map<String,String> map = null;
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            HttpEntity entity = response.getEntity();
            String responseStr = EntityUtils.toString(entity);
            map = new Gson().fromJson(responseStr, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
