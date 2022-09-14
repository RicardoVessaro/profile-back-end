package estudo.s.account.rest.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estudo.s.account.data.model.User;
import estudo.s.account.rest.Constants;
import estudo.s.account.rest.assembler.UserAssembler;
import estudo.s.account.rest.dto.UserDTO;
import estudo.s.account.service.UserService;
import estudo.s.ipsum.rest.CRUDControllerImpl;
import estudo.s.ipsum.rest.ResponseBuilder;


@RestController
@RequestMapping(Constants.USER_BASE_URL)
public class UserController extends CRUDControllerImpl<User, UserDTO, UUID> {

    public UserController(UserService service, UserAssembler assembler) {
        super(service, assembler);
    }

    @Override
    protected ResponseBuilder<User, UserDTO, UUID> responseBuilder() {
        return new ResponseBuilder<User, UserDTO, UUID>(getAssembler()) {};
    }

}
