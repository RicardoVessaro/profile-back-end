package estudo.s.account.rest.advice;

import org.springframework.http.HttpStatus;

public class AdviceResponse {

    private final HttpStatus httpStatus;
    private final String message;

    public AdviceResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
