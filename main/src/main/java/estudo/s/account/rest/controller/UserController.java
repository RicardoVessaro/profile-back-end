package estudo.s.account.rest.controller;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estudo.s.account.data.entity.User;
import estudo.s.account.rest.Constants;
import estudo.s.account.rest.assembler.UserAssembler;
import estudo.s.account.rest.dto.UserDTO;
import estudo.s.account.service.UserService;
import estudo.s.ipsum.rest.Controller;
import estudo.s.ipsum.rest.ResponseBuilder;


@RestController
@RequestMapping(Constants.USER_BASE_URL)
public class UserController implements Controller<UserDTO, UUID> {

    private final UserService service;

    private final UserAssembler assembler;

    @Autowired
    private ModelMapper modelMapper;

    public UserController(UserService service, UserAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserDTO>> insert(@RequestBody UserDTO body) {

        User entity = modelMapper.map(body, User.class);

        entity = service.insert(entity);

        return responseBuilder().createResponse(entity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UserDTO>>> findAll(Pageable pageable) {

        Page<User> entities = service.findAll(pageable);

        return responseBuilder().createResponse(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable UUID id) {
        Optional<User> optionalEntity = service.findById(id);

        if (optionalEntity.isPresent()) {
            return responseBuilder().createResponse(optionalEntity.get());
        }

        return responseBuilder().noContent();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> update(@PathVariable UUID id, @RequestBody UserDTO body) {

        User entityChanges = modelMapper.map(body, User.class);

        User entity = service.update(id, entityChanges);

        return responseBuilder().createResponse(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> delete(@PathVariable UUID id) {
        service.delete(id);

        return responseBuilder().noContent();
    }

    private ResponseBuilder<User, UserDTO, UUID> responseBuilder() {
        return new ResponseBuilder<User, UserDTO, UUID>(assembler) {};
    }

}
