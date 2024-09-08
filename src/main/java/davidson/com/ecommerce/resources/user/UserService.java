package davidson.com.ecommerce.resources.user;

import davidson.com.ecommerce.exceptions.*;
import davidson.com.ecommerce.resources.user.dtos.request.SignupRequestDto;
import davidson.com.ecommerce.resources.user.enums.Role;
import org.springframework.dao.DataIntegrityViolationException;
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
        if (existsByEmail(dto.email())) throw new ContentConflictException("Email already exists");
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.name(), dto.email(), encodedPassword, Role.CLIENT, true);
        return userRespository.save(user);
    }

    public User signupAdmin(SignupRequestDto dto, User admin) {
        if (existsByEmail(dto.email())) throw new ContentConflictException("Email already exists");
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.name(), dto.email(), encodedPassword, Role.ADMIN, true);
        user.setRegisteredBy(admin);
        return userRespository.save(user);
    }

    public User getById(Long id) {
        return userRespository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User> getAll() {
        List<User> users = userRespository.findAll();
        if(users.isEmpty()) throw new NoContentException("No users found");
        return users;
    }

    public void delete(Long id, User admin) {
        User user = (User) loadUserByUsername(getById(id).getEmail());
        if(!user.isActive()) throw new ContentConflictException("User is not active");

        try {
            userRespository.delete(user);
        }
        catch (DataIntegrityViolationException exception) {
            user.deactivate();
            userRespository.save(user);
        }
    }

    public String updatePassword(String email, String newPassword) {
        User user = (User) userRespository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRespository.save(user);
        return "Password updated successfully";
    }
}
