package net.cybercake.cyberapi.common.basic.logs;

public class LoggingException extends IllegalStateException{

    public LoggingException() {
        super();
    }

    public LoggingException(String message) {
        super(message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggingException(Throwable cause) {
        super(cause);
    }

}
