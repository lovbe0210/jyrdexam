package com.jyrd.exam.base.vo;

import lombok.Data;

@Data
public class PasswordInfo {
    private Integer userId;
    private String beforePwd;
    private String nowpwd;
    private String confirmNowPwd;
}
