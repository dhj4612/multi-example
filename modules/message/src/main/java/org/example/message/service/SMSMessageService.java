package org.example.message.service;

import org.example.message.constant.SMSMessageType;

public interface SMSMessageService {

    /**
     * 发送短信验证码
     *
     * @param phone    手机号
     * @param sendType 发送类型
     */
    void sendCode(String phone, SMSMessageType sendType);

    /**
     * 校验短信验证码
     *
     * @param phone      手机号
     * @param code 验证码
     * @param sendType   发送类型
     * @return true 校验通过
     */
    boolean verifyCode(String phone, String code, SMSMessageType sendType);
}
