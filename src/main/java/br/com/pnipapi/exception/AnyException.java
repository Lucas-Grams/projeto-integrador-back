package br.com.pnipapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AnyException extends RuntimeException {

    Exception exc;
    String msgUser;

    public AnyException(String msgLog, String msgUser) {
        super(msgLog);
        this.msgUser = msgUser.isEmpty() ? "Ocorreu um erro, tente novamente!" : msgUser;
    }

    public AnyException(Exception e, String msgUser) {
        super(e.getMessage());
        this.exc = e;
        this.msgUser = msgUser.isEmpty() ? "Ocorreu um erro, tente novamente!" : msgUser;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public Exception getException() {
        return exc;
    }

}
