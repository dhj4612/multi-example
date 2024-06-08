package org.example.core.config.spring.core.covert;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StringTimestampToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(source)), ZoneId.systemDefault());
    }
}
