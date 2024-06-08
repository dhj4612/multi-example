package org.example.core.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JacksonUtil {
    private static final ObjectMapper ObjectMapper = SpringApplicationContextUtils.getBean(ObjectMapper.class);

    public static String toJsonStr(Object value) {
        try {
            return ObjectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObj(String jsonStr, Class<T> clazz) {
        if (StrUtil.isEmpty(jsonStr)) {
            return null;
        }
        try {
            return ObjectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObj(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        try {
            return ObjectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> TypeReference<T> newTypeRef() {
        return new TypeReference<>() {
        };
    }

    public static <T> T parseObj(String jsonStr, TypeReference<T> typeRef) {
        try {
            return ObjectMapper.readValue(jsonStr, typeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            return ObjectMapper.readValue(text, ObjectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
