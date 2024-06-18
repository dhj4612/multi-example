package org.example.message.strategy.impl;

import org.example.message.dto.SendOrVerifyOfCodeDTO;
import org.example.message.strategy.SMSStrategy;
import org.springframework.stereotype.Component;

@Component
public class TencentYunSMSStrategy implements SMSStrategy {
    @Override
    public String name() {
        return "TENCENT_YUM";
    }

    @Override
    public void sendCode(SendOrVerifyOfCodeDTO dto) {

    }
}
