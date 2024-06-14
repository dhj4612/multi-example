package org.example.message.constant;

public final class SMSMessageRedisKey {
    private SMSMessageRedisKey() {
    }

    public static final String SendCodeRedisKey = "sms:code:";

    /**
     * 获取验证码发送缓存key
     * @param type 发送类型
     * @param source 发送来源
     */
    public static String getSendCodeRedisKey(SMSMessageType type, String source) {
        return SendCodeRedisKey + type.name().toLowerCase() + ":" + source.toLowerCase() + ":";
    }
}
