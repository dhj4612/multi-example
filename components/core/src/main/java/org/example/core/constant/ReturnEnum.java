package org.example.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnEnum {
    OK(200, "success"),
    ERR(500, "fail");

    private final int code;
    private final String msg;
}
