package org.example;

import lombok.RequiredArgsConstructor;
import org.example.core.utils.JacksonUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class MainTest {

    public static String intToStrByLen(Integer i, Integer len, String fix) {
        String strValue = String.valueOf(i);
        int strLen = strValue.length();
        if (strLen == len) {
            return strValue;
        }
        if (strLen > len) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < (len - strLen); j++) {
            sb.append(fix);
        }
        return sb.append(strValue).toString();
    }


    public static List<Map<String, Object>> getEmoplyeesMoney(List<Map<String, Object>> emoplyees) {
        return emoplyees.stream().map(item -> {
                    int moneytype = Integer.parseInt(java.lang.String.valueOf(item.get("moneytype")));
                    if (moneytype == 0) {
                        return Map.of("name", item.get("name"),
                                "money", new BigDecimal(java.lang.String.valueOf(item.get("monthmoney"))));
                    } else {
                        BigDecimal hourmoney = new BigDecimal(java.lang.String.valueOf(item.get("hourmoney")));
                        BigDecimal hours = new BigDecimal(java.lang.String.valueOf(item.get("hours")));

                        return Map.of("name", item.get("name"),
                                "money", hourmoney.multiply(hours));
                    }
                }).sorted(Comparator.comparing((Map<String, Object> o) -> ((BigDecimal) o.get("money"))).reversed())
                .toList();
    }


    public static void saveMap(Map<String, String> map) {
        String jsonStr = JacksonUtil.toJsonStr(map);
        String escapedJsonString = jsonStr.replace("'", "\\'");

        String sql = "INSERT INTO testa (jsonstr) VALUE ('%s')".formatted(escapedJsonString);
    }

    @RequiredArgsConstructor
    enum UserStatus {
        A(0, "A"),
        B(1, "B");

        private final int code;
        private final String desc;
    }

    public static int getValue(int i) {
        int result = 0;
        switch (i) {
            case 1:
                result = result + i;
            case 2:
                result = result + i * 2;
            case 3:
                result = result + i * 3;
        }
        return result;
    }

    public static void main(String[] args) {
        //UserStatus a = UserStatus.valueOf("A");
        //UserStatus a1 = UserStatus.valueOf("A");
        //
        //System.out.println(a.equals(a1));
        //System.out.println(getValue(2));

        //List<Map<String, Object>> employees = Arrays.asList(
        //        new HashMap<>() {{
        //            put("name", "张三");
        //            put("month", 3);
        //            put("moneytype", 0);
        //            put("monthmoney", 5000.0);
        //            put("hourmoney", 0.0);
        //            put("hours", 0);
        //        }},
        //        new HashMap<>() {{
        //            put("name", "李四");
        //            put("month", 5);
        //            put("moneytype", 1);
        //            put("monthmoney", 0.0);
        //            put("hourmoney", 50.0);
        //            put("hours", 200);
        //        }},
        //        new HashMap<>() {{
        //            put("name", "王二");
        //            put("month", 6);
        //            put("moneytype", 0);
        //            put("monthmoney", 6000.0);
        //            put("hourmoney", 0.0);
        //            put("hours", 0);
        //        }},
        //        new HashMap<>() {{
        //            put("name", "刘五");
        //            put("month", 11);
        //            put("moneytype", 1);
        //            put("monthmoney", 0.0);
        //            put("hourmoney", 45.0);
        //            put("hours", 150);
        //        }}
        //);
        //
        //List<Map<String, Object>> result = getEmoplyeesMoney(employees);
        //result.forEach(System.out::println);

        System.out.println(intToStrByLen(123, 6, "0"));    // 结果 “000123”
        System.out.println(intToStrByLen(123, 7, "0"));     //  结果 “0000123”
        System.out.println(intToStrByLen(11, 5, "#"));     // 结果 “###11”
    }
}
