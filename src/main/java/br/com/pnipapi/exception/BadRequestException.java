package br.com.pnipapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private String msgUser;

    public BadRequestException() {}

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msgLog, String msgUser) {
        super(msgLog);
        this.msgUser = msgUser;
    }

    public String getMsgUser() {
        return msgUser;
    }
}
