package com.lcy.reggie.common;


/**
 * 自定义业务异常类
 */
public class BusinessException extends RuntimeException{
    public BusinessException(String massage){
        super(massage);
    }
}
