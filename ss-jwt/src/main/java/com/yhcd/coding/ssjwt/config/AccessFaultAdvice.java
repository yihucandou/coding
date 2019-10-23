package com.yhcd.coding.ssjwt.config;

import com.yhcd.coding.ssjwt.model.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hukai
 */
@RestControllerAdvice(basePackages = {"com.qykj.qiyego.business.management"})
public class AccessFaultAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    public Object accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return Result.failed("权限不足");
    }

}
