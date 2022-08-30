package estudo.s.ipsum.exception;

import org.springframework.http.HttpStatus;

public class IpsumException extends RuntimeException {

    private final HttpStatus httpStatus;

    public IpsumException(String message, HttpStatus httpStatus) {
        super(message);

        this.httpStatus = httpStatus;
    }

    public IpsumException(String message) {
        super(message);

        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static IpsumException notFound(String message) {
        return new IpsumException(message, HttpStatus.NOT_FOUND);
    }
}
