package davidson.com.ecommerce.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User, Long> {
}
