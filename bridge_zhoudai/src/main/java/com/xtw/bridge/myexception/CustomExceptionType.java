package com.xtw.bridge.myexception;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: 异常类型
 */
public enum CustomExceptionType {

    USER_INPUT_ERROR(400,"您输入的数据格式错误或您没有权限访问资源！"),
    SYSTEM_ERROR (500,"系统出现异常，请您稍后再试或联系管理员！");

    private String description;//异常类型中文描述

    private int code; //code

    /**
     *
     * @param code   状态码
     * @param description   描述信息
     */
    CustomExceptionType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
