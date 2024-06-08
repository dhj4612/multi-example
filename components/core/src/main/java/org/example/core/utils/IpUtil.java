package org.example.core.utils;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class IpUtil {

    static final PathMatchingResourcePatternResolver ClassPathResolver = new PathMatchingResourcePatternResolver();
    static final Resource resource;

    static {
        try {
            resource = ClassPathResolver.getResource("/static/ip2region.xdb");
        } catch (Exception e) {
            log.error("ip2region 初始化异常 =>", e);
            throw new RuntimeException("ip2region 初始化异常", e);
        }
    }

    public static String getAddrByIp(String ip) {
        Searcher searcher = null;
        try {
            searcher = Searcher.newWithBuffer(Files.readAllBytes(resource.getFile().toPath()));
            String region = searcher.search(ip);
            if (StrUtil.isBlank(region)) {
                return "unknown";
            }
            return region;
        } catch (Exception e) {
            log.error("根据ip => {} 获取地址异常：", ip, e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException ignored) {
                }
            }
        }
        return "unknown";
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : getMultistageReverseProxyIp(ip);
    }

    public static String getMultistageReverseProxyIp(String ip) {
        if (ip.indexOf(",") > 0) { // 反向代理
            final String[] ips = ip.trim().split(",");
            for (String sub : ips) {
                if (!"unknown".equalsIgnoreCase(sub)) {
                    ip = sub;
                    break;
                }
            }
        }
        return ip;
    }
}
