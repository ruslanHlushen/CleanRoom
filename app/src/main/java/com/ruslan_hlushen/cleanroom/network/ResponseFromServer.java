package com.ruslan_hlushen.cleanroom.network;

/**
 * Created by Руслан on 30.11.2016.
 */
public class ResponseFromServer<T> {

    public static final Integer ERROR_STATUS = 0;
    public static final Integer OK_STATUS = 1;

    private Integer status;
    private T data;
    private String error_message;


    public Integer getStatus() { return status; }


    public T getData() { return data; }


    public String getError_message() { return error_message; }
}
