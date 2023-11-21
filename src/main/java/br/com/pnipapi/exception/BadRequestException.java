package br.com.pnipapi.exception;

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
