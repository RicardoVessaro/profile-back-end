package estudo.s.account.service.impl.account;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import estudo.s.account.data.model.User;
import estudo.s.account.data.respository.UserRepository;
import estudo.s.account.service.UserService;
import estudo.s.ipsum.service.IpsumService;
import estudo.s.ipsum.service.RequiredField;
import estudo.s.ipsum.service.RequiredFieldValidator;

@Service
@Transactional
public class UserServiceImpl extends IpsumService<User, UUID> implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    public RequiredFieldValidator getRequiredFieldValidator(User entity) {
        return new RequiredFieldValidator(entity, "User", List.of(
            new RequiredField("getName", "Name"),
            new RequiredField("getPassword", "Password")
        ));
    }
}
