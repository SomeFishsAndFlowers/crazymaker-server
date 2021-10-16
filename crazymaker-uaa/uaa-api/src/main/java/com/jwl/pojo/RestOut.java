package com.jwl.pojo;


import lombok.Data;

/**
 * @author wenlo
 */
@Data
public class RestOut<T> {

    private T data;
    private String msg;
    private long code;

    private RestOut() {

    }

    public static <T> RestOut<T> success(T data) {
        RestOut<T> restOut = new RestOut<>();
        restOut.data = data;
        restOut.code = 200;
        return restOut;
    }

    public RestOut<T> setRespMsg(String msg) {
        this.msg = msg;
        return this;
    }

}
