package org.example.security.config;

import lombok.RequiredArgsConstructor;
import org.example.core.config.json.XssProperties;
import org.example.core.utils.ApiResourcesUtil;
import org.example.security.core.annotation.Anonymous;
import org.example.security.core.filter.XssFilter;
import org.example.security.core.handler.SecurityAccessDeniedHandler;
import org.example.security.core.handler.SecurityAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 启用方法级别的安全控制 => @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * token 过滤器，过滤未携带 token 的用户以及设置当前用户信息到 SecurityContext 中
     */
    private final OncePerRequestFilter authenticationTokenFilter;

    private final ApiResourcesUtil apiResourcesUtil;

    private final XssProperties xssProperties;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 忽略授权的地址列表
        List<String> allowResources = apiResourcesUtil.getApiResourceByAnnotation(Anonymous.class);
        http
                .headers(headers ->
                        headers.contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'; object-src 'none';"))
                )
                .cors(Customizer.withDefaults())

                // xss 防护
                .addFilterBefore(new XssFilter(xssProperties), UsernamePasswordAuthenticationFilter.class)

                // 身份验证过滤器
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)

                // 禁用 session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(allowResources.toArray(new String[0])).permitAll() // 允许匿名访问的请求
                        .requestMatchers("/actuator/**").permitAll() // 允许监控端点的请求
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 允许所有 OPTIONS 请求
                        .anyRequest()
                        .authenticated() // 其余请求全部需要验证
                )
                .exceptionHandling(ex -> {
                    // 未登录异常处理器，即 AuthenticationException
                    ex.authenticationEntryPoint(new SecurityAuthenticationEntryPoint());
                    // 未授权异常处理器，即 AccessDeniedException
                    ex.accessDeniedHandler(new SecurityAccessDeniedHandler());
                })
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable) // csrf 禁用（基于 token 认证不需要 csrf）
                .formLogin(AbstractHttpConfigurer::disable); // 表单认证禁用
        return http.build();
    }
}
