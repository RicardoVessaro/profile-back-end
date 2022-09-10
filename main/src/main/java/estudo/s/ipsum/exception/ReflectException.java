package estudo.s.ipsum.exception;

import org.springframework.http.HttpStatus;

public class ReflectException extends IpsumException{

    public ReflectException(String message, Object... args) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, args);
    }
    
}
