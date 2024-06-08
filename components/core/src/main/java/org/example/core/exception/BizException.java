package org.example.core.exception;

import lombok.Getter;
import org.example.core.constant.ReturnEnum;

/**
 * 业务异常
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;
    private final String msg;

    public BizException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizException(String msg) {
        super(msg);
        this.code = ReturnEnum.ERR.getCode();
        this.msg = msg;
    }
}
