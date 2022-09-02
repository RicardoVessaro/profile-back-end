package estudo.s.account.service.impl.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import estudo.s.account.data.entity.User;
import estudo.s.account.service.UserService;
import estudo.s.ipsum.exception.IpsumException;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserService service;
    
    private User entity;

    @BeforeEach
    public void setup() {
        entity = service.insert(new User("user", "1234"));
    }

    @Test
    public void testInsertAndFindById() {
        Optional<User> optionalEntity = service.findById(entity.getId());

        assertThat(optionalEntity.isPresent()).isTrue();

        User expectedEntity = optionalEntity.get();

        assertThat(entity.equals(expectedEntity)).isTrue();
        assertThat(entity.getId().equals(expectedEntity.getId())).isTrue();
        assertThat(entity.getName().equals(expectedEntity.getName())).isTrue();
        assertThat(entity.getPassword().equals(expectedEntity.getPassword())).isTrue();
    }

    @Test
    public void testFindAll() {

        List<UUID> expectedIds = new ArrayList<>();

        expectedIds.add(entity.getId());
		expectedIds.add(service.insert(new User("user1", "111")).getId());
        expectedIds.add(service.insert(new User("user2", "222")).getId());
        expectedIds.add(service.insert(new User("user3", "333")).getId());

        Page<User> entities = service.findAll(Pageable.ofSize(3));

		for (User entity: entities) {
            assertThat(entity.getId()).isIn(expectedIds);
        }
    }

    @Test
    public void testUpdate() {

        entity.setName("Updated User");
        entity.setPassword("4567");

        service.update(entity.getId(), entity);

        User updatedEntity = service.findById(entity.getId()).get();

        assertThat(entity.equals(updatedEntity)).isTrue();
        assertThat(entity.getId().equals(updatedEntity.getId())).isTrue();
        assertThat(entity.getName().equals(updatedEntity.getName())).isTrue();
        assertThat(entity.getPassword().equals(updatedEntity.getPassword())).isTrue();

    }

    @Test
    public void testUpdateSetIdWhenItIsEmptyInEntity() {

        UUID id = entity.getId();

        entity.setId(null);

        User updatedEntity = service.update(id, entity);

        assertThat(id.equals(updatedEntity.getId())).isTrue();
    }
    
    @Test
    public void testUpdateThrowsExceptionWhenGivenIdIsDifferentEntityId() {
        
        entity.setId(UUID.randomUUID());
        
        UUID givenId = UUID.randomUUID();

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            service.update(givenId, entity);
        });

        String expectedMessage =  new StringBuilder()
            .append("ID '" + givenId +"' ")
            .append("is different of entity ")
            .append("id '" + entity.getId() + "' ")
            .toString();

        assertThat(ipsumException.getMessage().equals(expectedMessage)).isTrue();

    }

    @Test
    public void testUpdateThrowsExceptionWhenCantFindEntity() {
        entity.setId(UUID.randomUUID());

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            service.update(entity.getId(), entity);
        });

        String expectedMessage = "Could not find Entity with id '" + entity.getId() + "'";

        assertThat(ipsumException.getMessage().equals(expectedMessage)).isTrue();
    }

    @Test
    public void testDelete() {

        service.delete(entity.getId());

        Optional<User> optionalEntity = service.findById(entity.getId());

        assertThat(optionalEntity.isEmpty()).isTrue();
    }

    @Test
    public void testDeleteThrowsExceptionWhenCantFindEntity() {
        entity.setId(UUID.randomUUID());

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            service.delete(entity.getId());
        });

        String expectedMessage = "Could not find Entity with id '" + entity.getId() + "'";

        assertThat(ipsumException.getMessage().equals(expectedMessage)).isTrue();
    }

}
