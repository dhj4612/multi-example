package org.example.auth.constant;

public final class AuthenticationRedisKey {
    public static final String LoginSessionRedisKey = "login:session:%s:%s";

    public static String getClientLoginSessionRedisKey(String token) {
        return LoginSessionRedisKey + UserType.C.name().toLowerCase() + ":";
    }

    public static String getSystemLoginSessionRedisKey(String token) {
        return LoginSessionRedisKey + UserType.S.name().toLowerCase() + ":";
    }

    public static String getLoginSessionRedisKey(UserType type, String token) {
        return LoginSessionRedisKey.formatted(type.name().toLowerCase(), token);
    }
}
