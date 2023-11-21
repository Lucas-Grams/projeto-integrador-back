package br.com.pnipapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class NotFoundException extends RuntimeException {

    private String msgUser;

    public NotFoundException(String msgLog, String msgUser) {
        super(msgLog);
        this.msgUser = msgUser;
    }

    public NotFoundException(String msgUser) {
        super(msgUser);
        this.msgUser = msgUser;
    }

    public String getMsgUser() {
        return msgUser;
    }

}
