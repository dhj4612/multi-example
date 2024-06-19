package org.example.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtil {

    @Value("${spring.profiles.active}")
    private String active;

    public boolean isDev() {
        return "dev".equalsIgnoreCase(active);
    }
}
