package br.com.pnipapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    private String msgUser;

    public ConflictException(String msgLog, String msgUser) {
        super(msgLog);
        this.msgUser = msgUser;
    }

    public String getMsgUser() {
        return msgUser;
    }

}
