package org.example.message.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SMSVerifyCodeType {
    /**
     * 登录
     */
    Login,

    /**
     * 注册
     */
    Register,
}
