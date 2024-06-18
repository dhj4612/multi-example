package org.example.message.strategy.impl;

import org.example.message.dto.SendOrVerifyOfCodeDTO;
import org.example.message.strategy.SMSStrategy;
import org.springframework.stereotype.Component;

@Component
public class AliYunSMSStrategy implements SMSStrategy {

    @Override
    public String name() {
        return "ALI_YUN";
    }

    @Override
    public void sendCode(SendOrVerifyOfCodeDTO dto) {

    }
}
