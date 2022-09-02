package estudo.s.account.resource.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import estudo.s.account.data.entity.User;
import estudo.s.account.resource.dto.UserDTO;
import estudo.s.account.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserController controller;

    @MockBean
    UserService service;

    @Test
    public void testInsert() {
        
        UserDTO body = new UserDTO();
        body.setName("user");
        body.setPassword("1234");
        
        User entity = new User();
        entity.setId(UUID.randomUUID());

        when(service.insert(any())).thenReturn(entity);

        ResponseEntity<EntityModel<UserDTO>> response = controller.insert(body);

        Link editLink = linkTo(methodOn(UserController.class).findById(entity.getId())).withRel("edit");
        assertThat(editLink).isIn(response.getBody().getLinks());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}
