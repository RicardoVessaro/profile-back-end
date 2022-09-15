package estudo.s.ipsumintegrationtest.constants;

public enum HttpStatus {
    
    OK(200, "OK"),
    CREATED(201, "CREATED"),
    NO_CONTENT(204, "NO_CONTENT"),

    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND")
    ;

    public final int code;
    public final String description;

    private HttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
