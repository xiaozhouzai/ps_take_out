package com.lcy.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        //判断抛出异常信息中是否包含关键字
        if(exception.getMessage().contains("Duplicate entry")){
            //将异常信息按空格分割
            String[] split = exception.getMessage().split(" ");
            //拼接新的异常信息
            String msg = split[2]+"已被占用";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 处理业务异常
     * @param exception
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public R<String> exceptionHandler(BusinessException exception){
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
