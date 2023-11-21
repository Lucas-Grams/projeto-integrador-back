package br.com.pnipapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MethodArgumentNotValidException extends RuntimeException {

    public MethodArgumentNotValidException(String s) {
        super(s);
    }

}
