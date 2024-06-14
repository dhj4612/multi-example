package org.example.message.strategy;

import org.example.message.dto.SendSMSConfigDTO;

public final class SmsContext {
    private SmsContext() {
    }

    public static SmsStrategy getStrategy(SendSMSConfigDTO config) {
        return null;
    }
}
