package org.example.auth.utils;

import cn.hutool.core.map.MapBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.time.DateUtils;
import org.example.auth.constant.UserType;
import org.example.auth.model.dto.SecurityUserDTO;
import org.example.auth.model.entity.User;
import org.example.security.config.JwtConfig;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 */
public abstract class JwtTokenUtil {
    public static JWTVerifier VERIFIER;

    static {
        Algorithm algorithm = Algorithm.HMAC256(JwtConfig.Secret.getBytes());
        VERIFIER = JWT.require(algorithm)
                .withIssuer("com.example.dhj")
                .build();
    }

    /**
     * 创建访问令牌
     */
    public static String createAccessToken(Map<String, String> userInfo) {
        return JWT.create()
                .withJWTId(userInfo.get("id"))
                .withIssuer("com.example.dhj")
                .withClaim("type", userInfo.get("type"))
                .withExpiresAt(DateUtils.addMilliseconds(new Date(), JwtConfig.Expire))
                .sign(Algorithm.HMAC256(JwtConfig.Secret.getBytes()));
    }

    public static Map<String, String> builderUserInfo(User user) {
        return MapBuilder.<String, String>create()
                .put("id", String.valueOf(user.getId()))
                .put("type", user.getUserType().name())
                .build();
    }

    /**
     * 创建 token 刷新令牌，过期时间为7天
     */
    public static String createRefreshToken(Long userId) {
        return JWT.create()
                .withJWTId(String.valueOf(userId))
                .withExpiresAt(DateUtils.addDays(new Date(), 7))
                .sign(Algorithm.HMAC256(JwtConfig.Secret.getBytes()));
    }

    /**
     * 校验 token 是否合法
     */
    public static DecodedJWT verifyAccessToken(String accessToken) {
        return VERIFIER.verify(accessToken);
    }

    /**
     * 校验 token 合法性并返回用户信息
     */
    public static UserDetails verifyAccessTokenAndGet(String accessToken) {
        DecodedJWT jwt = verifyAccessToken(accessToken);
        SecurityUserDTO user = new SecurityUserDTO();
        user.setId(Integer.valueOf(jwt.getId()));
        user.setType(UserType.valueOf(jwt.getClaim("type").asString()));
        return user;
    }
}
