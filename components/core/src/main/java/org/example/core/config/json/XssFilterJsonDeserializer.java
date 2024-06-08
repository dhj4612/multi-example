package org.example.core.config.json;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.core.utils.HttpServletUtil;
import org.example.core.utils.XssUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;

/**
 * Xss Json 过滤
 */
@RequiredArgsConstructor
public class XssFilterJsonDeserializer extends JsonDeserializer<String> {
    private final XssProperties properties;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        if (StrUtil.isBlank(value)) {
            return null;
        }

        HttpServletRequest request = HttpServletUtil.getHttpServletRequest();
        if (request == null) {
            return value;
        }

        boolean allow = properties.getExcludes().stream()
                .anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, request.getRequestURI()));
        if (allow) {
            return value;
        }

        return XssUtils.filter(value);
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }
}
