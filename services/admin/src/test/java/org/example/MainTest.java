package org.example;

import lombok.RequiredArgsConstructor;

public class MainTest {

    @RequiredArgsConstructor
    enum UserStatus {
        A(0, "A"),
        B(1, "B");

        private final int code;
        private final String desc;
    }

    public static void main(String[] args) {
        UserStatus a = UserStatus.valueOf("A");
        UserStatus a1 = UserStatus.valueOf("A");

        System.out.println(a.equals(a1));
    }
}
