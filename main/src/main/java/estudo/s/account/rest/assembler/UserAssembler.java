package estudo.s.account.rest.assembler;

import java.util.UUID;

import org.springframework.stereotype.Component;

import estudo.s.account.rest.controller.UserController;
import estudo.s.account.rest.dto.UserDTO;
import estudo.s.ipsum.rest.Assembler;
import estudo.s.ipsum.rest.CRUDController;

@Component
public class UserAssembler extends Assembler<UserDTO, UUID>{

    @Override
    public Class<? extends CRUDController<UserDTO, UUID>> controllerClass() {
        return UserController.class;
    }
    
}
