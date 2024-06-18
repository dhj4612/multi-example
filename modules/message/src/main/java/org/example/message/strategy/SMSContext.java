package org.example.message.strategy;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.core.exception.BizException;
import org.example.message.dto.SMSConfigDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Component
public class SMSContext {

    public static final String SmsConfigCacheKey = "sms:config:cache:key";
    public static final String SmsConfigRoundKey = "sms:config:round:key";

    @Resource
    private List<SMSStrategy> strategyList;

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
            config = next(configList);
        }
        return findStrategy(config);
    }

    private SMSStrategy findStrategy(SMSConfigDTO config) {
        if (CollectionUtils.isEmpty(this.strategyList)) {
            return null;
        }
        return this.strategyList.stream()
                .filter(strategy -> Objects.equals(strategy.name(), config.getChannel()))
                .findFirst()
                .orElseThrow(() -> new BizException("短信通道不存在"));
    }

    private SMSConfigDTO next(List<SMSConfigDTO> configList) {
        Long idx = redisTemplate.opsForValue().increment(SmsConfigRoundKey);
        Objects.requireNonNull(idx, "获取短信配置失败");
        return configList.get((int) (idx % configList.size()));
    }
}
