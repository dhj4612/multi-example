package org.example.message.service.impl;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.core.exception.BizException;
import org.example.core.utils.AesUtil;
import org.example.core.utils.ValidationUtils;
import org.example.message.constant.SMSMessageRedisKey;
import org.example.message.dto.SendOrVerifyOfCodeDTO;
import org.example.message.service.SMSMessageService;
import org.example.message.validate.SendCodeValidate;
import org.example.message.validate.VerifyCodeValidate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SMSMessageServiceImpl implements SMSMessageService {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    public String sendCode(SendOrVerifyOfCodeDTO dto) {
        ValidationUtils.warpValidate(dto, SendCodeValidate.class).throwIfNotPassed();
        if (dto.getConfig() != null) {
            ValidationUtils.warpValidate(dto.getConfig()).throwIfNotPassed();
        }

        final String key = SMSMessageRedisKey
                .getSendCodeRedisKey(dto.getType(), dto.getSource()) + AesUtil.encrypt(dto.getPhone());
        final String cacheCode = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasLength(cacheCode)) {
            throw new BizException("验证码发送频繁");
        }
        final String code = RandomUtil.randomNumbers(6);

        redisTemplate.opsForValue().set(
                key,
                code,
                2,
                TimeUnit.MINUTES);

        return code;
    }

    @Override
    public boolean verifyCode(SendOrVerifyOfCodeDTO dto) {
        ValidationUtils.warpValidate(dto, VerifyCodeValidate.class).throwIfNotPassed();

        final String key = SMSMessageRedisKey
                .getSendCodeRedisKey(dto.getType(), dto.getSource()) + AesUtil.encrypt(dto.getCode());
        String cacheCode = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasLength(cacheCode)) {
            return false;
        }
        if (cacheCode.equals(dto.getCode())) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
