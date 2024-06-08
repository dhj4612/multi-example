package org.example.redis.core;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.api.LockExecutor;
import org.example.redis.api.LockExecutorResult;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockExecutor implements LockExecutor {

    private final RedissonClient redissonClient;
    private static final Consumer<Throwable> DefaultExceptionHandler = e -> log.error("分布式锁执行异常：", e);

    @Override
    public <T> LockExecutorResult<T> execution(Supplier<String> keyGenerator, Callable<T> call) {
        return execution(keyGenerator, call, 15, TimeUnit.SECONDS);
    }

    @Override
    public <T> LockExecutorResult<T> tryExecution(Supplier<String> keyGenerator, Callable<T> call) {
        return tryExecution(keyGenerator, call, 5, 15, TimeUnit.SECONDS);
    }

    @Override
    public <T> LockExecutorResult<T> execution(Supplier<String> lockKeyGenerator,
                                               Callable<T> call,
                                               long releaseTime,
                                               TimeUnit unit) {
        String key = "sync:lock:" + lockKeyGenerator.get();
        RLock lock = redissonClient.getLock(key);
        lock.lock(releaseTime, unit);
        try {
            T result = call.call();
            return LockExecutorResult.createSuccess(result);
        } catch (Throwable e) {
            return LockExecutorResult.createFailure(e);
        } finally {
            safeRelease(lock);
        }
    }

    @Override
    public <T> LockExecutorResult<T> tryExecution(Supplier<String> keyGenerator,
                                                  Callable<T> call,
                                                  long waitTime,
                                                  long releaseTime,
                                                  TimeUnit unit) {
        String key = "sync:tryLock:" + keyGenerator.get();
        RLock lock = redissonClient.getLock(key);
        try {
            if (lock.tryLock(waitTime, releaseTime, unit)) {
                T result = call.call();
                return LockExecutorResult.createSuccess(result);
            }
            return LockExecutorResult.createFailure(false);
        } catch (InterruptedException e) {
            return LockExecutorResult.createFailure(false);
        } catch (Throwable e) {
            return LockExecutorResult.createFailure(e);
        } finally {
            safeRelease(lock);
        }
    }

    @Override
    public void execution(Supplier<String> keyGenerator, Runnable run) {
        execution(
                keyGenerator,
                run,
                15,
                TimeUnit.SECONDS);
    }

    @Override
    public void tryExecution(Supplier<String> keyGenerator, Runnable run) {
        tryExecution(
                keyGenerator,
                run,
                5,
                15,
                TimeUnit.SECONDS);
    }

    @Override
    public void execution(Supplier<String> keyGenerator,
                          Runnable run,
                          long releaseTime,
                          TimeUnit unit) {
        String key = "sync:lock:" + keyGenerator.get();
        RLock lock = redissonClient.getLock(key);
        lock.lock(releaseTime, unit);
        try {
            run.run();
        } catch (Throwable e) {
            DefaultExceptionHandler.accept(e);
            throw new RuntimeException(e);
        } finally {
            safeRelease(lock);
        }
    }

    @Override
    public void tryExecution(Supplier<String> keyGenerator,
                             Runnable run,
                             long waitTime,
                             long releaseTime,
                             TimeUnit unit) {
        String key = "sync:tryLock:" + keyGenerator.get();
        RLock lock = redissonClient.getLock(key);
        try {
            if (lock.tryLock(waitTime, releaseTime, unit)) {
                run.run();
            }
        } catch (InterruptedException ignored) {
        } catch (Throwable e) {
            DefaultExceptionHandler.accept(e);
            throw new RuntimeException(e);
        } finally {
            safeRelease(lock);
        }
    }

    private static void safeRelease(RLock lock) {
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
