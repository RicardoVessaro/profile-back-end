package estudo.s.account.service.impl.account;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import estudo.s.account.data.entity.User;
import estudo.s.account.data.respository.UserRepository;
import estudo.s.account.exception.IpsumException;
import estudo.s.account.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User insert(User entity) {
        return repository.save(entity);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public User update(UUID id, User entityChanges) {

        if(entityChanges.getId() == null) {
            entityChanges.setId(id);

        } else if (!entityChanges.getId().equals(id)) {
            String message = new StringBuilder()
                    .append("ID '" + id.toString() +"' ")
                    .append("is different of entity ")
                    .append("id '" + entityChanges.getId() + "' ")
                    .toString();
            throw new IpsumException(message);
        }

        User entity = findByIdOrElseThrow(id);

        modelMapper.map(entityChanges, entity);

        return repository.save(entity);
    }

    @Override
    public UUID delete(UUID id) {
        User entity = findByIdOrElseThrow(id);

        repository.delete(entity);

        return id;
    }

    private User findByIdOrElseThrow(UUID id) {
        User entity = findById(id)
                .orElseThrow(() -> IpsumException.notFound("Could not find Entity with id '" + id.toString() + "'"));
        return entity;
    }
}
