package br.com.pnipapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private String msgUser;

    public ForbiddenException(String msgLog, String msgUser) {
        super(msgLog);
        this.msgUser = msgUser;
    }

    public String getMsgUser() {
        return msgUser;
    }
}
