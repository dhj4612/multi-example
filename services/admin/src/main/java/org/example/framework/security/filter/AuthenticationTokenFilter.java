package org.example.framework.security.filter;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.auth.constant.AuthenticationRedisKey;
import org.example.auth.constant.UserType;
import org.example.auth.model.dto.SecurityUserDTO;
import org.example.auth.utils.JwtTokenUtil;
import org.example.core.utils.HttpServletUtil;
import org.example.security.config.JwtConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) {
        try {
            String authorizationHeader = request.getHeader(JwtConfig.Key);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(null);
            SecurityContextHolder.setContext(context);

            // 没有携带 token，放行空的 context，交给 security 处理
            if (StringUtils.isBlank(authorizationHeader)
                    || !authorizationHeader.startsWith("Bearer ")) { // 遵循 OAuth2 认证协议请求头
                chain.doFilter(request, response);
                return;
            }

            String jwt = authorizationHeader.substring(7);
            DecodedJWT decodedJWT = JwtTokenUtil.verifyAccessToken(jwt);

            final String key = AuthenticationRedisKey
                    .getLoginSessionRedisKey(UserType.valueOf(decodedJWT.getClaim("type").asString()), jwt);
            String jsonData = redisTemplate.opsForValue().get(key);
            if (StringUtils.isBlank(jsonData) || !JSONUtil.isTypeJSON(jsonData)) {
                log.error("用户信息错误 => {}", jsonData);
                chain.doFilter(request, response);
                return;
            }
            SecurityUserDTO user = JSONUtil.toBean(jsonData, SecurityUserDTO.class);
            if (!user.isEnabled()) {
                HttpServletUtil.responseExceptionJsonData(response, "用户已禁用");
                chain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            context.setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            HttpServletUtil.responseExceptionJsonData(response, "非法授权");
        } catch (Throwable e) {
            HttpServletUtil.responseExceptionJsonData(response, "登录校验失败");
        }
    }
}
