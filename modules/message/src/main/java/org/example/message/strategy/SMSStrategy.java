package org.example.message.strategy;

import java.util.Map;

/**
 * 短信发送策略抽象
 */
public interface SMSStrategy {

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @param params 参数
     */
    void sendCode(String phone, String code, Map<String, Object> params);
}
