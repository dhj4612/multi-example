package org.example.message.service;

import org.example.message.dto.SendOrVerifyOfCodeDTO;

public interface SMSMessageService {

    /**
     * 发送短信验证码
     * @return 返回发送的验证码
     */
    String sendCode(SendOrVerifyOfCodeDTO dto);

    /**
     * 校验短信验证码
     */
    boolean verifyCode(SendOrVerifyOfCodeDTO dto);
}
