package org.example.message.strategy;

import java.util.Map;

/**
 * 短信发送策略抽象
 */
public interface SmsStrategy {

    /**
     * 发送短信
     *
     * @param phone 手机号
     * @param params 参数
     */
    void send(String phone, Map<String, String> params);
}
