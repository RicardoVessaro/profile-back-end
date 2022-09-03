package estudo.s.account.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import estudo.s.account.data.entity.User;

public interface UserService {

    User insert(User entity);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(UUID id);

    User update(UUID id, User entityChanges);

    UUID delete(UUID id);
}
