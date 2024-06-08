package org.example.core.base;

import lombok.Getter;
import org.example.core.constant.ReturnEnum;

@Getter
public final class ReturnData<T> {
    private final int code;
    private final String msg;
    private final T data;

    private ReturnData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ReturnData(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public static <T> ReturnData<T> ok() {
        return new ReturnData<>(ReturnEnum.OK.getCode(), ReturnEnum.OK.getMsg());
    }

    public static <T> ReturnData<T> ok(T data) {
        return new ReturnData<>(ReturnEnum.OK.getCode(), ReturnEnum.OK.getMsg(), data);
    }

    public static ReturnData<?> okMsg(String msg) {
        return new ReturnData<>(ReturnEnum.OK.getCode(), msg);
    }

    public static <T> ReturnData<T> ok(String msg, T data) {
        return new ReturnData<>(ReturnEnum.OK.getCode(), msg, data);
    }

    public static ReturnData<?> err() {
        return new ReturnData<>(ReturnEnum.ERR.getCode(), ReturnEnum.ERR.getMsg());
    }

    public static <T> ReturnData<T> err(T data) {
        return new ReturnData<>(ReturnEnum.ERR.getCode(), ReturnEnum.OK.getMsg(), data);
    }

    public static <T> ReturnData<T> errMsg(String msg) {
        return new ReturnData<>(ReturnEnum.ERR.getCode(), msg);
    }

    public static <T> ReturnData<T> err(String msg, T data) {
        return new ReturnData<>(ReturnEnum.ERR.getCode(), msg, data);
    }

    public static <T> ReturnData<T> err(int code, String msg) {
        return new ReturnData<>(code, msg);
    }

    public static <T> ReturnData<T> err(int code, String msg, T data) {
        return new ReturnData<>(code, msg, data);
    }
}
