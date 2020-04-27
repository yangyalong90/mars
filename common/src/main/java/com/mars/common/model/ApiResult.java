package com.mars.common.model;

public class ApiResult {

    public static final String SUCCESS_CODE = "1";
    public static final String FAIL_CODE = "0";

    private String code;
    private String message;
    private Object data;

    public static ApiResult success(){
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(SUCCESS_CODE);
        return apiResult;
    }

    public static ApiResult success(String message){
        ApiResult success = success();
        success.setMessage(message);
        return success;
    }

    public static ApiResult success(Object data){
        ApiResult success = success();
        success.setData(data);
        return success;
    }

    public static ApiResult success(String message, Object data){
        ApiResult success = success();
        success.setMessage(message);
        success.setData(data);
        return success;
    }

    public static ApiResult fail(){
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(FAIL_CODE);
        return apiResult;
    }

    public static ApiResult fail(String message){
        ApiResult fail = fail();
        fail.setMessage(message);
        return fail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
}
