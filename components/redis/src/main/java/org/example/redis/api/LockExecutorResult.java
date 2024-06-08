package org.example.redis.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class LockExecutorResult<T> {
    private T result;
    private boolean isSuccess;
    private boolean isAcquire;
    private Throwable cause;

    public static <T> LockExecutorResult<T> createSuccess() {
        return createSuccess(true);
    }

    public static <T> LockExecutorResult<T> createSuccess(T result) {
        return createSuccess(result, true);
    }

    public static <T> LockExecutorResult<T> createSuccess(boolean isAcquire) {
        return createSuccess(null, isAcquire);
    }

    public static <T> LockExecutorResult<T> createSuccess(T result, boolean isAcquire) {
        return LockExecutorResult.<T>builder()
                .result(result)
                .isAcquire(isAcquire)
                .isSuccess(true)
                .build();
    }

    public static <T> LockExecutorResult<T> createFailure() {
        return createFailure(true);
    }

    public static <T> LockExecutorResult<T> createFailure(boolean isAcquire) {
        return createFailure(null, isAcquire);
    }

    public static <T> LockExecutorResult<T> createFailure(Throwable e) {
        return createFailure(e, true);
    }

    public static <T> LockExecutorResult<T> createFailure(Throwable e, boolean isAcquire) {
        return LockExecutorResult.<T>builder()
                .result(null)
                .isAcquire(isAcquire)
                .isSuccess(false)
                .cause(e)
                .build();
    }
}
