package org.example.core.config.spring.config;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.core.base.ReturnData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dhj
 */
@Slf4j
@ConditionalOnWebApplication
@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object>, Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> clazz) {
        // 自定义异常不处理
        return !returnType.getContainingClass().isAssignableFrom(GlobalExceptionHandler.class);
    }

    @Override
    public Object beforeBodyWrite(Object result,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> clazz,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        return switch (result) {
            case null -> ReturnData.ok();
            case String _ -> {
                response.getHeaders().set("Content-Type", ContentType.JSON.getValue());
                yield JSONUtil.toJsonStr(ReturnData.ok(response));
            }
            case ResponseEntity<?> _, ReturnData<?> _ -> result;
            default -> ReturnData.ok(result);
        };
    }
}
