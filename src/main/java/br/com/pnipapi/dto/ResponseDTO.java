package br.com.pnipapi.dto;

import lombok.Value;

@Value
public class ResponseDTO<T> {

    String msg;
    Status status;
    T data;

    public ResponseDTO(String msg, Status s, T data) {
        this.msg = msg;
        this.status = s;
        this.data = data;
    }

    public static <T> ResponseDTO<T> ok(String msg, T data) {
        return new ResponseDTO<>(msg, Status.SUCCESS, data);
    }

    public static ResponseDTO<Void> err(String msg) {
        return new ResponseDTO<>(msg, Status.ERROR, null);
    }

    public enum Status { SUCCESS, WARNING, ERROR }
}
