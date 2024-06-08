package org.example.core.config.spring.core.covert;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LongTimestampToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
    @Override
    public LocalDateTime convert(Long source) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(source), ZoneId.systemDefault());
    }
}
