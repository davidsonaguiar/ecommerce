package davidson.com.ecommerce.resources.user;

import davidson.com.ecommerce.common.LinkBuilder;
import davidson.com.ecommerce.resources.user.dtos.request.SigninRequestDto;
import davidson.com.ecommerce.resources.user.dtos.request.SignupRequestDto;
import davidson.com.ecommerce.resources.user.dtos.response.SigninResponseDto;
import davidson.com.ecommerce.resources.user.dtos.response.SignupResponseDto;
import davidson.com.ecommerce.security.TokenService;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final LinkBuilder linkBuilder;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService, LinkBuilder linkBuilder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.linkBuilder = linkBuilder;
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody @Valid SigninRequestDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok().body(new SigninResponseDto(token));
    }

    @PostMapping(value = "/signup")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto dto) {
        User user = userService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SignupResponseDto.fromEntity(user));
    }

    @PostMapping(value = "/signup/admin")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<SignupResponseDto> signupAdmin(@RequestBody @Valid SignupRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();

        User user = userService.signupAdmin(dto, admin);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SignupResponseDto.fromEntity(user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<SignupResponseDto>> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        EntityModel<SignupResponseDto> entityModel = EntityModel.of(SignupResponseDto.fromEntity(user));
        entityModel.add(linkBuilder.linkToUser(user.getId()).withRel("user"));
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping
    @Cacheable("users")
    public ResponseEntity<List<EntityModel<SignupResponseDto>>> getAllUsers() {
        List<User> users = userService.getAll();
        if (users.isEmpty()) return ResponseEntity.ok(List.of());
        List<EntityModel<SignupResponseDto>> entityModels = users.stream().map(user -> {
            EntityModel<SignupResponseDto> entityModel = EntityModel.of(SignupResponseDto.fromEntity(user));
            entityModel.add(linkBuilder.linkToUser(user.getId()).withRel("user"));
            return entityModel;
        }).toList();
        return ResponseEntity.ok(entityModels);
    }

    @DeleteMapping(value = "/{id}")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
