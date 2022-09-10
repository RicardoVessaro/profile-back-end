package estudo.s.ipsum.exception;

import org.springframework.http.HttpStatus;

public class IpsumException extends RuntimeException {

    private final HttpStatus httpStatus;

    public IpsumException(String message, HttpStatus httpStatus, Object... args) {
        super(String.format(message, args));

        this.httpStatus = httpStatus;
    }

    public IpsumException(String message, Object... args) {
        super(String.format(message, args));

        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static IpsumException notFound(String message, Object... args) {
        return new IpsumException(message, HttpStatus.NOT_FOUND, args);
    }
}
