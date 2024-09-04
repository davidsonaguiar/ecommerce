package davidson.com.ecommerce.resources.user;

import davidson.com.ecommerce.exceptions.ContentConflictException;
import davidson.com.ecommerce.exceptions.ResourceNotFoundException;
import davidson.com.ecommerce.resources.user.dtos.request.SignupRequestDto;
import davidson.com.ecommerce.resources.user.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRespository userRespository;

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRespository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRespository.existsByEmail(email);
    }

    public User signup(SignupRequestDto dto) {
        if(existsByEmail(dto.email())) throw new ContentConflictException("Email already exists");
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.name(), dto.email(), encodedPassword, Role.CLIENT);
        return userRespository.save(user);
    }

    public User signupAdmin(SignupRequestDto dto, User admin) {
        if(existsByEmail(dto.email())) throw new ContentConflictException("Email already exists");
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.name(), dto.email(), encodedPassword, Role.ADMIN);
        user.setRegisteredBy(admin);
        return userRespository.save(user);
    }

    public User getById(Long id) {
        return userRespository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAll() {
        return userRespository.findAll();
    }

    public void delete(Long id) {
        if(!userRespository.existsById(id)) throw new ResourceNotFoundException("User not found");
        userRespository.deleteById(id);
    }
}
