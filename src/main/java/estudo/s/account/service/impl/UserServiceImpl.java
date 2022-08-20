package estudo.s.account.service.impl;

import estudo.s.account.entity.User;
import estudo.s.account.respository.UserRepository;
import estudo.s.account.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User insert(User entity) {
        return repository.save(entity);
    }
}
