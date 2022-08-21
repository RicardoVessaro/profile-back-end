package estudo.s.account.service;

import estudo.s.account.data.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User insert(User entity);

    List<User> findAll();

    Optional<User> findById(UUID id);

    User update(UUID id, User entityChanges);

    UUID delete(UUID id);
}
