package br.com.pnipapi.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static <T> ResponseDTO<T> ok(T data) {
        return new ResponseDTO<>(null, Status.SUCCESS, data);
    }

    public static <T> ResponseDTO<T> ok(String msg) {
        return new ResponseDTO<>(msg, Status.SUCCESS, null);
    }

    public static <T> ResponseDTO<T> err(String msg) {
        return new ResponseDTO<>(msg, Status.ERROR, null);
    }

    public enum Status { SUCCESS, WARNING, ERROR }

    public T transform(TypeReference<T> reference) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            T object = mapper.readValue(mapper.writeValueAsString(getData()), reference);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}
