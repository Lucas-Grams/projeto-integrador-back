package br.com.pnipapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public final class TooManyRequestsException extends RuntimeException {

    private String msgUser;

    public TooManyRequestsException(String msgLog, String msgUser) {
        super(msgLog);
        this.msgUser = msgUser;
    }

    public String getMsgUser() {
        return msgUser;
    }

}
