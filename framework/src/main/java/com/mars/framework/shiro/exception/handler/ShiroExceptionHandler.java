package com.mars.framework.shiro.exception.handler;

import com.mars.framework.jwt.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ShiroExceptionHandler {

    private ExceptionResultParse resultParse;

    public static final Map<Class<? extends Exception>, ErrorInfo> ERROR_INFO_MAP = new HashMap<>();

    public static final ErrorInfo UNKNOWN_ERROR = new ErrorInfo(200, "程序出错了");

    public static final ErrorInfo RUNTIME_ERROR = new ErrorInfo(200, "运行时异常");

    static {

        // 鉴权错误
        ERROR_INFO_MAP.put(AuthorizationException.class, new ErrorInfo(401, "你没有权限"));

        // 认证超期
        ERROR_INFO_MAP.put(TokenExpiredException.class, new ErrorInfo(401, "认证超期，重新登陆"));
    }

    public ShiroExceptionHandler(ExceptionResultParse resultParse) {
        this.resultParse = resultParse;
    }

    @ExceptionHandler({TokenExpiredException.class, AuthorizationException.class})
    public void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {

        Map.Entry<Class<? extends Exception>, ErrorInfo> errorInfoEntry = ERROR_INFO_MAP.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(e.getClass()))
                .findFirst().get();

        ErrorInfo errorInfo = errorInfoEntry == null ? UNKNOWN_ERROR : errorInfoEntry.getValue();
        doHandle(request, response, errorInfo.code, errorInfo.message);
    }

    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthentication(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        doHandle(request, response, 405, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void exception(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        doHandle(request, response, UNKNOWN_ERROR.code, UNKNOWN_ERROR.message);
    }

    @ExceptionHandler(RuntimeException.class)
    public void runtimeException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        doHandle(request, response, RUNTIME_ERROR.code, RUNTIME_ERROR.message);
    }

    public void doHandle(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {

        response.setStatus(status);
        response.getWriter().write(resultParse.parse(message));
    }


    static class ErrorInfo {
        Integer code;
        String message;

        public ErrorInfo(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}
