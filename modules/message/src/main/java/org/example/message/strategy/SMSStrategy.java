package org.example.message.strategy;

import org.example.message.dto.SendOrVerifyOfCodeDTO;

/**
 * 短信发送策略抽象
 */
public interface SMSStrategy {

    /**
     * 发送短信验证码
     */
    void sendCode(SendOrVerifyOfCodeDTO dto);
}
