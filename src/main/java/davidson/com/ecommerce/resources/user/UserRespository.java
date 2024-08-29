package davidson.com.ecommerce.resources.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRespository extends JpaRepository<User, Long> {
    Optional<UserDetails> findByEmail(String email);
    boolean existsByEmail(String email);
}
