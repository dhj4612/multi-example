package org.example.message.strategy;

public final class SmsContext {
    private SmsContext() {
    }

    public static SmsStrategy getStrategy(String channel) {
        return null;
    }
}
