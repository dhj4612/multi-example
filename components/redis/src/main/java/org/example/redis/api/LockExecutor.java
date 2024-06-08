package org.example.redis.api;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface LockExecutor {

    <T> LockExecutorResult<T> execution(Supplier<String> keyGenerator, Callable<T> call);

    <T> LockExecutorResult<T> tryExecution(Supplier<String> keyGenerator, Callable<T> call);

    <T> LockExecutorResult<T> execution(Supplier<String> keyGenerator,
                                        Callable<T> call,
                                        long releaseTime,
                                        TimeUnit unit);

    <T> LockExecutorResult<T> tryExecution(Supplier<String> keyGenerator,
                                           Callable<T> call,
                                           long waitTime,
                                           long releaseTime,
                                           TimeUnit unit);

    void execution(Supplier<String> keyGenerator,
                   Runnable run);

    void tryExecution(Supplier<String> keyGenerator,
                      Runnable run);

    void execution(Supplier<String> keyGenerator,
                   Runnable run,
                   long releaseTime,
                   TimeUnit unit);

    void tryExecution(Supplier<String> keyGenerator,
                      Runnable run,
                      long waitTime,
                      long releaseTime,
                      TimeUnit unit);
}
