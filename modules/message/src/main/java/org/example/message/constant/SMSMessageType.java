package org.example.message.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SMSMessageType {
    /**
     * 登录发送验证码
     */
    Login,

    /**
     * 注册发送验证码
     */
    Register,
}
