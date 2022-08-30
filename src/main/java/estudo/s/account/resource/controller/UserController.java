package estudo.s.account.resource.controller;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
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
import estudo.s.account.resource.ResourceConstants;
import estudo.s.account.resource.assembler.UserAssembler;
import estudo.s.account.resource.dto.UserDTO;
import estudo.s.account.service.UserService;

@RestController
@RequestMapping(ResourceConstants.USER_BASE_URL)
public class UserController {

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

        return createResponse(entity);
    }

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {

        Page<User> entities = service.findAll(pageable);

        return createResponse(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable UUID id) {
        Optional<User> optionalEntity = service.findById(id);

        if (optionalEntity.isPresent()) {
            return createResponse(optionalEntity.get());
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> update(@PathVariable UUID id, @RequestBody UserDTO body) {

        User entityChanges = modelMapper.map(body, User.class);

        User entity = service.update(id, entityChanges);

        return createResponse(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<EntityModel<UserDTO>> createResponse(User entity) {
        return createResponse(entity, HttpStatus.OK);
    }

    private ResponseEntity<EntityModel<UserDTO>> createResponse(User entity, HttpStatus httpStatus) {
        UserDTO dto = modelMapper.map(entity, UserDTO.class);

        return new ResponseEntity<>(assembler.toModel(dto), httpStatus);
    }

    private ResponseEntity<?> createResponse(Page<User> entities) {
        return createResponse(entities, HttpStatus.OK);
    }

    private ResponseEntity<?> createResponse(Page<User> entities, HttpStatus httpStatus) {
        Page<UserDTO> dtos = entities.map(entity -> {
                    return modelMapper.map(entity, UserDTO.class);
                });

        return ResponseEntity
            .status(httpStatus)
            .body(assembler.toPagedModel(dtos));
    }

}
