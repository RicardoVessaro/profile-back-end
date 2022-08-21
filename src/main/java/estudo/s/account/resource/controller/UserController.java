package estudo.s.account.resource.controller;

import estudo.s.account.data.entity.User;
import estudo.s.account.resource.ResourceConstants;
import estudo.s.account.resource.dto.UserDTO;
import estudo.s.account.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(ResourceConstants.USER_BASE_URL)
public class UserController {

    private final UserService service;

    @Autowired
    private ModelMapper modelMapper;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody UserDTO body) {

        User entity = modelMapper.map(body, User.class);

        entity = service.insert(entity);

        UserDTO dto = modelMapper.map(entity, UserDTO.class);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // TODO mudar metodo para usar paginação
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {

        List<UserDTO> dtos = service.findAll().stream()
                .map(entity -> {
                    return modelMapper.map(entity, UserDTO.class);
                }).toList();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        Optional<User> optionalEntity = service.findById(id);

        if(optionalEntity.isPresent()) {
            UserDTO dto = modelMapper.map(optionalEntity.get(), UserDTO.class);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable UUID id, @RequestBody UserDTO body) {

        User entityChanges = modelMapper.map(body, User.class);

        User entity = service.update(id, entityChanges);

        UserDTO dto = modelMapper.map(entity, UserDTO.class);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
