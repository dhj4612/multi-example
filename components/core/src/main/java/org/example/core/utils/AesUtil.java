package org.example.core.utils;

import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class AesUtil implements InitializingBean {

    private static String AesKey;

    @Value("${aes-key}")
    private String aesKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        AesKey = this.aesKey;
    }

    /**
     * 生成 AES 算法的密钥
     */
    public static String generateAesKey() {
        return Base64.getEncoder().encodeToString(SecureUtil.generateKey("AES").getEncoded());
    }

    public static String encrypt(String dataStr) {
        return encrypt(AesKey, dataStr);
    }

    public static String decrypt(String encryptDataStr) {
        return decrypt(AesKey, encryptDataStr);
    }

    /**
     * Aes 加密字符串数据
     */
    public static String encrypt(String key, String dataStr) {
        if (StringUtils.isBlank(key)) {
            key = AesKey;
        }
        byte[] encryptBytes = SecureUtil.aes(key.getBytes()).encrypt(dataStr.getBytes());
        return Base64.getEncoder().encodeToString(encryptBytes);
    }

    /**
     * Aes 解密字符串数据
     */
    public static String decrypt(String key, String encryptDataStr) {
        if (StringUtils.isBlank(key)) {
            key = AesKey;
        }
        byte[] decryptBytes = SecureUtil.aes(key.getBytes()).decrypt(Base64.getDecoder().decode(encryptDataStr));
        return new String(decryptBytes);
    }
}
