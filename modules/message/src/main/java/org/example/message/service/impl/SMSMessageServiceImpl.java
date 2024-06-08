package org.example.message.service.impl;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.core.exception.BizException;
import org.example.core.utils.AesUtil;
import org.example.message.constant.SMSMessageRedisKey;
import org.example.message.constant.SMSMessageType;
import org.example.message.service.SMSMessageService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SMSMessageServiceImpl implements SMSMessageService {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    public void sendCode(String phone, SMSMessageType sendType) {
        Assert.isTrue(StringUtils.hasLength(phone), "手机号不能为空");
        final String key = SMSMessageRedisKey.getSendCodeRedisKey(sendType) + AesUtil.encrypt(phone);
        final String cacheCode = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasLength(cacheCode)) {
            throw new BizException("验证码发送频繁");
        }

        redisTemplate.opsForValue().set(
                key,
                RandomUtil.randomNumbers(6),
                2,
                TimeUnit.MINUTES);
    }

    @Override
    public boolean verifyCode(String phone, String code, SMSMessageType sendType) {
        final String key = SMSMessageRedisKey.getSendCodeRedisKey(sendType) + AesUtil.encrypt(phone);
        String originalCode = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasLength(originalCode)) {
            return false;
        }
        redisTemplate.delete(key);
        return originalCode.equals(code);
    }
}
