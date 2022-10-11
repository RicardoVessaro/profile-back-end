package estudo.s.ipsumintegrationtest.exception;

public class IpsumIntegrationTestException extends RuntimeException {
    
    public IpsumIntegrationTestException(String message, Object... args) {
        super(String.format(message, args));
    }

}
