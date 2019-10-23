package com.yhcd.coding.ssjwt.model;

/**
 * @author hukai
 */
public class Result<T> {
    private Boolean success;
    private Integer code;
    private String message;
    private T data;

    public static Result failed(String message) {
        return new Result(false, message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(T data) {
        this.data = data;
    }
}
