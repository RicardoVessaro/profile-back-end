package estudo.s.account.resource.advice;

import estudo.s.account.exception.IpsumException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class IpsumAdvice {

    @ResponseBody
    @ExceptionHandler(IpsumException.class)
    ResponseEntity<AdviceResponse> ipsumHandler(IpsumException exception) {
        AdviceResponse adviceResponse = new AdviceResponse(exception.getMessage(), exception.getHttpStatus());

        return new ResponseEntity<>(adviceResponse, adviceResponse.getHttpStatus());
    }
}
