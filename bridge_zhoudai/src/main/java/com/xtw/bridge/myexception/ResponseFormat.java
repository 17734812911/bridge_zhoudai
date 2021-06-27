package com.xtw.bridge.myexception;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: 统一的JSON数据返回格式
 */
public class ResponseFormat {
    private boolean isok;  // 请求是否处理成功

    private int code;   // 请求响应状态码（200、400、500）

    private String message;  // 请求结果描述信息

    private Object data;    // 请求结果数据（通常用于查询操作）

    private ResponseFormat(){}

    public boolean isIsok() {
        return isok;
    }

    public void setIsok(boolean isok) {
        this.isok = isok;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    //请求出现异常时的响应数据封装
    public static ResponseFormat error(CustomException e) {
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setIsok(false);
        responseFormat.setCode(e.getCode());
        responseFormat.setMessage(e.getMessage());
        return responseFormat;
    }

    //请求出现异常时的响应数据封装
    public static ResponseFormat error(CustomExceptionType customExceptionType,
                                       String errorMessage) {
        ResponseFormat resultBean = new ResponseFormat();
        resultBean.setIsok(false);
        resultBean.setCode(customExceptionType.getCode());
        resultBean.setMessage(errorMessage);
        return resultBean;
    }

    //请求成功的响应，不带查询数据（用于删除、修改、新增接口）
    public static ResponseFormat success(String msg){
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setIsok(true);
        responseFormat.setCode(200);
        responseFormat.setMessage(msg);
        return responseFormat;
    }

    //请求成功的响应，带有查询数据（用于数据查询接口）
    public static ResponseFormat success(Object obj){
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setIsok(true);
        responseFormat.setCode(200);
        responseFormat.setMessage("请求响应成功!");
        responseFormat.setData(obj);
        return responseFormat;
    }

    //请求成功的响应，带有查询数据（用于数据查询接口）
    public static ResponseFormat success(String message, Object data){
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setIsok(true);
        responseFormat.setCode(200);
        responseFormat.setMessage(message);
        responseFormat.setData(data);
        return responseFormat;
    }
}
