package org.example.message.constant;

public final class SMSMessageRedisKey {
    public static final String SendCodeRedisKey = "sms:code:";

    public static String getLoginSendCodeRedisKey() {
        return SendCodeRedisKey + SMSMessageType.CLogin.name().toLowerCase() + ":";
    }

    public static String getSendCodeRedisKey(SMSMessageType type) {
        return SendCodeRedisKey + type.name().toLowerCase() + ":";
    }
}
