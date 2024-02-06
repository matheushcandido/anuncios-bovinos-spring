package insetec.backend.repositories;

import insetec.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String login);

    Optional<User> getByLogin(String login);

}
