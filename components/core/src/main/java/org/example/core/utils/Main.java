package org.example.core.utils;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class Main {
    public static void main(String[] args) throws Throwable {
        patternResolverExample();
    }

    static void patternResolverExample() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("/static/ip2region.xdb");
        Searcher searcher = Searcher.newWithBuffer(resource.getContentAsByteArray());
        String search = searcher.search("221.117.122.220");
        System.out.println(search);
    }
}
