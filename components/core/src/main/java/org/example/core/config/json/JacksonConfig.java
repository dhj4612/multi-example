package org.example.core.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

    //private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final XssProperties properties;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 把 Long 类型序列化为 String，防止精度丢失
            builder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        final ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 注册 JavaTimeModule 以支持 Java 8 日期和时间 API，并配置日期格式
        //final JavaTimeModule javaTimeModule = new JavaTimeModule();
        //javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        //javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
        //objectMapper.registerModule(javaTimeModule);

        // 自定义序列化和反序列化：将时间戳转换为 LocalDateTime
        final SimpleModule module = new SimpleModule();

        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser,
                                             DeserializationContext deserializationContext) throws IOException {
                long timestamp = Long.parseLong(jsonParser.getValueAsString());
                return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
            }
        });

        module.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime localDateTime,
                                  JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
                jsonGenerator.writeNumber(timestamp);
            }
        });

        // xss 防护
        module.addDeserializer(String.class, new XssFilterJsonDeserializer(properties));

        objectMapper.registerModule(module);

        // 设置序列化时忽略空值
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 配置日期格式为字符串而不是时间戳
        //objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }
}
