package estudo.s.account.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import estudo.s.account.data.entity.User;
import estudo.s.account.rest.controller.UserController;
import estudo.s.account.rest.dto.UserDTO;
import estudo.s.account.service.UserService;

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

    @Test
    public void testFindAll() {
        
        User entity1 = new User("User 1", "111");
        entity1.setId( UUID.randomUUID());

        User entity2 = new User("User 2", "222");
        entity2.setId( UUID.randomUUID());

        User entity3 = new User("User 3", "333");
        entity3.setId(UUID.randomUUID());
        
        Page<User> entities = new PageImpl<>(List.of(entity1, entity2, entity3));
        Pageable pageable = Pageable.ofSize(3);
        
        when(service.findAll(pageable)).thenReturn(entities);

        ResponseEntity<PagedModel<EntityModel<UserDTO>>> response = controller.findAll(pageable);

        Link insertLink = linkTo(methodOn(UserController.class).insert(null)).withRel("create");
        assertThat(insertLink).isIn(response.getBody().getLinks());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        for(EntityModel<UserDTO> item: response.getBody().getContent()) {
            Link editLink = linkTo(methodOn(UserController.class).findById(item.getContent().getId()))
                .withRel("edit");
            assertThat(editLink).isIn(item.getLinks());
        }
    }

    @Test
    public void testFindById() {

        User entity = new User("User", "1234");
        entity.setId(UUID.randomUUID());

        when(service.findById(entity.getId())).thenReturn(Optional.of(entity));

        ResponseEntity<EntityModel<UserDTO>> response = controller.findById(entity.getId());

        assertThat(response.getBody().getContent().getId()).isEqualTo(entity.getId());

        Link editLink = linkTo(methodOn(UserController.class).findById(entity.getId())).withRel("edit");
        assertThat(editLink).isIn(response.getBody().getLinks());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFindByIdMustReturnHttpStatusNoContentWhenNotFound() {

        when(service.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<UserDTO>> response = controller.findById(UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testUpdate() {

        UserDTO dto = new UserDTO();
        dto.setId(UUID.randomUUID());

        User entity = new User();
        entity.setId(dto.getId());
        
        when(service.update(eq(entity.getId()), any())).thenReturn(entity);

        ResponseEntity<EntityModel<UserDTO>> response = controller.update(dto.getId(), dto);

        Link editLink = linkTo(methodOn(UserController.class).findById(entity.getId())).withRel("edit");
        assertThat(editLink).isIn(response.getBody().getLinks());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(dto.getId()).isEqualTo(entity.getId());
    }

    @Test
    public void testDelete() {
        
        ResponseEntity<EntityModel<UserDTO>> response = controller.delete(UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
