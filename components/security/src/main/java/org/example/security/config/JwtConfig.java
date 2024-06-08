package org.example.security.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtConfig implements InitializingBean {
    @Value("${token.expire}")
    private int expire;
    public static int Expire;

    @Value("${token.secret}")
    private String secret;
    public static String Secret;

    @Value("${token.key}")
    public String key;
    public static String Key;

    @Override
    public void afterPropertiesSet() throws Exception {
        Expire = this.expire;
        Secret = this.secret;
        Key = this.key;
    }
}
