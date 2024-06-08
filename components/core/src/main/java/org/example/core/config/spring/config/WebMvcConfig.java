package org.example.core.config.spring.config;

import org.example.core.config.spring.core.covert.LongTimestampToLocalDateTimeConverter;
import org.example.core.config.spring.core.covert.StringTimestampToLocalDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 配置 form 表单参数的时间戳形式转换为 LocalDateTime
        registry.addConverter(new LongTimestampToLocalDateTimeConverter());
        registry.addConverter(new StringTimestampToLocalDateTimeConverter());
    }
}
