package org.example.message.strategy;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.message.dto.SMSConfigDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SMSContext {

    public static final String SmsConfigCacheKey = "sms:config:cache:key";
    public static final String SmsConfigRoundKey = "sms:config:round:key";

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    public SMSStrategy getStrategy(SMSConfigDTO config) {
        String configCache = redisTemplate.opsForValue().get(SmsConfigCacheKey);
        if (StringUtils.isBlank(configCache)) {
            // TODO 缓存没有，从数据库获取，存入缓存

            // TODO 数据库也没有 => throw new BizException("没有可用的短信配置");
        }

        List<SMSConfigDTO> configList = JSONUtil.toList(configCache, SMSConfigDTO.class);

        if (config == null) {
            // TODO 轮询短信配置
            redisTemplate.opsForValue().increment(SmsConfigCacheKey);
        }

        // TODO 根据短信配置获取短信发送策略
        return null;
    }
}
