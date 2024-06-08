package org.example.core.config.spring.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.core.base.ReturnData;
import org.example.core.exception.BizException;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dhj
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ReturnData<?> paramMissHandler(MethodArgumentTypeMismatchException ex) {
        MethodParameter parameter = ex.getParameter();
        String parameterName = parameter.getParameterName();
        log.error("参数类型不匹配：", ex);
        return ReturnData.errMsg("参数类型不匹配:" + parameterName);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ReturnData<?> paramMissHandler(ConstraintViolationException ex) {
        log.error("参数错误：", ex);
        String multipartErrorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining());
        return ReturnData.errMsg("参数错误：" + multipartErrorMessage);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ReturnData<?> illegalStateExceptionHandler(IllegalStateException ex) {
        log.error("状态异常：", ex);
        return ReturnData.errMsg(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ReturnData<?> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("参数非法：", ex);
        return ReturnData.errMsg(ex.getMessage());
    }

    /**
     * hibernate validate 校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ReturnData<?> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        log.error("参数校验异常：", ex);
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        Set<String> errs = errors.stream().map(err ->
                err instanceof FieldError fieldError ?
                        fieldError.getDefaultMessage() :
                        err.getDefaultMessage()).collect(Collectors.toSet());
        return ReturnData.errMsg(String.join(",", errs));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ReturnData<?> badCredentialsExceptionHandler(BadCredentialsException e) {
        log.error("认证异常：", e);
        return ReturnData.errMsg(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ReturnData<?> authenticationExceptionHandler(AccessDeniedException e) {
        log.error("授权异常：", e);
        return ReturnData.errMsg(e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public ReturnData<?> bizExceptionHandler(BizException e) {
        log.error("业务异常：", e);
        return ReturnData.errMsg(e.getMsg());
    }

    @ExceptionHandler(RuntimeException.class)
    public ReturnData<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("系统运行异常：", e);
        return ReturnData.errMsg(getDeepExceptionCause(e).getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ReturnData<?> defaultExceptionHandler(Exception e) {
        log.error("系统错误：", e);
        return ReturnData.errMsg("网络繁忙，稍后再试~");
    }

    /**
     * 获取深层次的异常原因
     */
    private static Throwable getDeepExceptionCause(Throwable e) {
        if (e.getCause() == null) {
            return e;
        }
        return getDeepExceptionCause(e);
    }
}
