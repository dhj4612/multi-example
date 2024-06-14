package org.example.message.strategy;

import org.example.message.dto.SendSMSConfigDTO;

public final class SMSContext {
    private SMSContext() {
    }

    public static SMSStrategy getStrategy(SendSMSConfigDTO config) {
        return null;
    }
}
