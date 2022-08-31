package estudo.s.account.service.impl.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import estudo.s.account.data.entity.User;
import estudo.s.account.service.UserService;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserService service;

    @Test
    public void testInsert() {
        User entityToInsert = new User("John", "1234");
        
        User entity = service.insert(entityToInsert);

        Optional<User> optionalEntity = service.findById(entity.getId());

        assertThat(optionalEntity.isPresent()).isTrue();

        entity = optionalEntity.get();

        assertThat(entityToInsert.equals(entity)).isTrue();
    }

}
