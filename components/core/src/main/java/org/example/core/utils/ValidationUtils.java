package org.example.core.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class ValidationUtils {
    private static final Validator validator;

    public static class ValidationResultProcess<T> {
        private final Set<ConstraintViolation<T>> result;
        private final T target;

        public ValidationResultProcess(Set<ConstraintViolation<T>> result, T target) {
            this.result = result;
            this.target = target;
        }

        public boolean passed() {
            return result == null || result.isEmpty();
        }

        public ValidationResultProcess<T> then(Consumer<T> action) {
            if (passed()) {
                action.accept(this.target);
            }
            return this;
        }

        public ValidationResultProcess<T> orElse(Consumer<String> action) {
            if (!passed()) {
                action.accept(splicingErrorMessage());
            }
            return this;
        }

        public String splicingErrorMessage() {
            if (!passed()) {
                return result.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(","));
            }
            return "";
        }

        public void throwIfNotPassed() {
            if (!passed()) {
                throw new RuntimeException(splicingErrorMessage());
            }
        }
    }

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            log.info("参数校验工具初始化失败：", e);
            throw e;
        }
    }

    public static <T> ValidationResultProcess<T> warpValidate(T target, Class<?>... groups) {
        return new ValidationResultProcess<>(validator.validate(target, groups), target);
    }

    public static <T> Set<ConstraintViolation<T>> validate(T target, Class<?>... groups) {
        return validator.validate(target);
    }
}
