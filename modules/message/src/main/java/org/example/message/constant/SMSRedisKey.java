package org.example.message.constant;

import org.example.core.utils.AesUtil;

public final class SMSRedisKey {
    private SMSRedisKey() {
    }

    public static final String VerifyCodeRedisKey = "sms:code:%s:%s:%s";

    /**
     * 获取验证码发送缓存key
     * @param type 发送类型
     * @param source 发送来源
     */
    public static String getVerifyCodeRedisKey(SMSVerifyCodeType type, String source, String phone) {
        return VerifyCodeRedisKey.formatted(type.name().toLowerCase(), source.toLowerCase(), AesUtil.encrypt(phone));
    }
}
