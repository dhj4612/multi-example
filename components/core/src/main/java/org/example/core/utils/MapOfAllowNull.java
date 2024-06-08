package org.example.core.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapOfAllowNull {
    public static Map<String, Object> of(Object... input) {
        Map<String, Object> resultMap;
        int len = input.length;
        if ((len & 1) != 0) {
            throw new RuntimeException("输入数量必须为偶数");
        }
        if (len == 0) {
            return Collections.emptyMap();
        }
        resultMap = new HashMap<>((int) (len / 0.75F) + 1);
        for (int i = 0; i < len - 1; i += 2) {
            resultMap.put(String.valueOf(input[i]), input[i + 1]);
        }
        return resultMap;
    }
}
