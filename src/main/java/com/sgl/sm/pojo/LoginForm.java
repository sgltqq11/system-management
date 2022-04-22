package com.sgl.sm.pojo;

import lombok.Data;

//对前端登录进行封装的类
/**
 * @project: ssm_sms
 * @description: 用户登录表单信息
 */

@Data
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;
}
