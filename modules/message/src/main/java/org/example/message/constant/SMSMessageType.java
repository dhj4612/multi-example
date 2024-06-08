package org.example.message.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SMSMessageType {
    /**
     * C端登录发送验证码
     */
    CLogin,

    /**
     * C端注册发送验证码
     */
    CRegister,

    /**
     * 系统登录发送验证码
     */
    SLogin,

    /**
     * 系统用户注册发送验证码
     */
    SRegister;
}
