package estudo.s.ipsum.service;

import static estudo.s.ipsum.exception.ExceptionMessage.REQUIRED_FIELDS_NOT_GIVEN;

import java.util.List;

import estudo.s.ipsum.exception.IpsumException;
import estudo.s.ipsum.reflection.Reflection;

public class RequiredFieldValidator {
    
    private List<RequiredField> requiredFields;
    private Object toValidate;
    private String alias;

    public RequiredFieldValidator(Object toValidate, String alias, List<RequiredField> requiredFields) {
        this.toValidate = toValidate;
        this.alias = alias;
        this.requiredFields = requiredFields;        
    }

    public void validate() {
        String requiredFieldMessage = "";

        for (RequiredField requiredField: requiredFields) {
            Reflection reflection = new Reflection(toValidate);

            if(reflection.invoke(requiredField.getGetter()) == null) {
                requiredFieldMessage += String.format("\n%s", requiredField.getAlias());
            }
        }

        if(!requiredFieldMessage.equals("")) {
            throw new IpsumException(REQUIRED_FIELDS_NOT_GIVEN, alias, requiredFieldMessage);
        }
    }   

}
