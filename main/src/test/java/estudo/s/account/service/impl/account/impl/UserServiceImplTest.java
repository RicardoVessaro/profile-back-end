package estudo.s.account.service.impl.account.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import estudo.s.account.data.model.User;
import estudo.s.account.service.impl.account.UserServiceImpl;
import estudo.s.ipsum.service.CRUDServiceImpl;
import estudo.s.ipsum.service.CRUDServiceImplTest;


public class UserServiceImplTest extends CRUDServiceImplTest<User, UUID> {

    @Autowired
    private UserServiceImpl service;

    @Override
    public void assertEntityEquals(User actual, User expected) {
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }   

	@Override
	public CRUDServiceImpl<User, UUID> getService() {
		return service;
	}

    @Override
    public User newEntity() {
        return new User("user", "1234");
    }

    @Override
    public void editEntity() {
        getEntity().setName("Updated User");
        getEntity().setPassword("4567");
    }

    @Override
    public UUID differentId() {
        return UUID.randomUUID();
    }

    @Override
    public List<User> newEntities() {
        return List.of(
            new User("User 1", "1111"),
            new User("User 2", "2222"),
            new User("User 3", "3333")
        );
    }

    @Override
    public User emptyEntity() {
        return new User();
    }

    @Override
    public List<String> getRequiredFields() {
       return List.of(
            "Name", 
            "Password"
       );
    }

}
