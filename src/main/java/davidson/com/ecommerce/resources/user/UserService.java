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
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRespository userRespository;

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> user = userRespository.findByEmail(username);
        if(user.isEmpty()) throw new UsernameNotFoundException("User not found");
        return user.get();
    }

    public boolean existsByEmail(String email) {
        return userRespository.existsByEmail(email);
    }

    public User signup(SignupRequestDto dto) throws ContentConflictException {
        if (existsByEmail(dto.email())) throw new ContentConflictException("Email already exists");
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.name(), dto.email(), encodedPassword, Role.CLIENT, true);
        return userRespository.save(user);
    }

    public User signupAdmin(SignupRequestDto dto, User admin) throws ContentConflictException {
        if (existsByEmail(dto.email())) throw new ContentConflictException("Email already exists");
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.name(), dto.email(), encodedPassword, Role.ADMIN, true);
        user.setRegisteredBy(admin);
        return userRespository.save(user);
    }

    public User getById(Long id) throws ResourceNotFoundException {
        Optional<User> user = userRespository.findById(id);
        if (user.isEmpty()) throw new ResourceNotFoundException("User not found");
        return user.get();
    }

    public List<User> getAll() throws NoContentException {
        List<User> users = userRespository.findAll();
        if(users.isEmpty()) throw new NoContentException("No users found");
        return users;
    }

    public void delete(Long id) throws ResourceNotFoundException, ContentConflictException {
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

    public String updatePassword(String email, String newPassword) throws ResourceNotFoundException {
        Optional<UserDetails> userDetails =  userRespository.findByEmail(email);
        if (userDetails.isEmpty()) throw new ResourceNotFoundException("User not found");
        User user = (User) userDetails.get();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRespository.save(user);
        return "Password updated successfully";
    }
}
