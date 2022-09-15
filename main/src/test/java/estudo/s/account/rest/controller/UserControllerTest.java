package estudo.s.account.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import estudo.s.account.data.model.User;
import estudo.s.account.rest.dto.UserDTO;
import estudo.s.account.service.UserService;
import estudo.s.ipsum.rest.CRUDControllerImpl;
import estudo.s.ipsum.rest.CRUDControllerImplTest;
import estudo.s.ipsum.service.CRUDService;

public class UserControllerTest extends CRUDControllerImplTest<User, UserDTO, UUID> {

    @Autowired
    UserController controller;

    @MockBean
    UserService service;

    @Override
    public CRUDControllerImpl<User, UserDTO, UUID> getController() {
        return controller;
    }

    @Override
    public CRUDService<User, UUID> getService() {
        return service;
    }

    @Override
    public UserDTO newDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(UUID.randomUUID());
        dto.setName("user");
        dto.setPassword("1234");
        
        return dto;
    }

    @Override
    public User newEntity() {
        User entity = new User();
        entity.setId(UUID.randomUUID());
        entity.setName("user");
        entity.setPassword("1234");

        return entity;
    }

    @Override
    public List<User> newEntities() {
        User entity1 = new User("User 1", "111");
        entity1.setId( UUID.randomUUID());

        User entity2 = new User("User 2", "222");
        entity2.setId( UUID.randomUUID());

        User entity3 = new User("User 3", "333");
        entity3.setId(UUID.randomUUID());

        return List.of(entity1, entity2, entity3);
    }

    @Override
    public UUID differentId() {
        return UUID.randomUUID();
    }

}
