package org.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class AdminApplicationTest {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Test
    void test() {
        Long increment = redisTemplate.opsForValue().increment("incr:cache:key");
        System.out.println(increment);
    }
}
