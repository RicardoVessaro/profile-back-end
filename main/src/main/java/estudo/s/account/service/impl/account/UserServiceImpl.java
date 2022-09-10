package estudo.s.account.service.impl.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import estudo.s.account.data.entity.User;
import estudo.s.account.data.respository.UserRepository;
import estudo.s.account.service.UserService;
import estudo.s.ipsum.exception.IpsumException;
import estudo.s.ipsum.service.RequiredField;
import estudo.s.ipsum.service.RequiredFieldValidator;

import static estudo.s.ipsum.exception.ExceptionMessage.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User insert(User entity) {

        getRequiredFieldValidator(entity).validate();

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
            throw new IpsumException(PATH_ID_DIFFERENT_ENTITY_ID, id, entityChanges.getId());
        }

        User entity = findByIdOrElseThrow(id);
        
        getRequiredFieldValidator(entityChanges).validate();

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
                .orElseThrow(() -> IpsumException.notFound(COULD_NOT_FOUND_ENTITY_WITH_ID, id));
        return entity;
    }

    private RequiredFieldValidator getRequiredFieldValidator(User entity) {
        return new RequiredFieldValidator(entity, "User", List.of(
            new RequiredField("getName", "Name"),
            new RequiredField("getPassword", "Password")
        ));
    }
}
