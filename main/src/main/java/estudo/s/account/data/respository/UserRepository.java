package estudo.s.account.data.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import estudo.s.account.data.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
