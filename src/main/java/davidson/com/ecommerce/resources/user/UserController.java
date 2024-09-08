package davidson.com.ecommerce.resources.user;

import davidson.com.ecommerce.exceptions.UnauthorizedException;
import davidson.com.ecommerce.resources.user.dtos.request.ResetPasswordRequestDto;
import davidson.com.ecommerce.resources.user.dtos.request.SigninRequestDto;
import davidson.com.ecommerce.resources.user.dtos.request.SignupRequestDto;
import davidson.com.ecommerce.resources.user.dtos.request.UpdatePasswordRequestDto;
import davidson.com.ecommerce.resources.user.dtos.response.GetUserResponseDto;
import davidson.com.ecommerce.resources.user.dtos.response.SigninResponseDto;
import davidson.com.ecommerce.security.TokenService;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;



@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailService emailService;


    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService, EmailService emailService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }


    @PostMapping(value = "/signup")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequestDto dto) {
        User user = userService.signup(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }


    @PostMapping(value = "/signup/admin")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<Void> signupAdmin(@RequestBody @Valid SignupRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();

        User user = userService.signupAdmin(dto, admin);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @PostMapping(value = "/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody @Valid SigninRequestDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = authenticationManager.authenticate(usernamePassword);

        User user = (User) authentication.getPrincipal();
        if (!user.isActive()) throw new UnauthorizedException("User is not active");

        String token = tokenService.generateToken(user);
        return ResponseEntity.ok().body(new SigninResponseDto(token));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDto dto) {
        UserDetails userDetails = userService.loadUserByUsername(dto.email());

        User user = (User) userDetails;
        if (!user.isActive()) throw new UnauthorizedException("User is not active");

        String token = tokenService.generateToken(user);

        String url = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/reset-password")
                .queryParam("token", token)
                .toUriString();
        String message = emailService.sendEmail(dto.email(), "Reset Password", "Url for reset password: " + url);

        return ResponseEntity.ok(message);
    }


    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody @Valid UpdatePasswordRequestDto dto) {
        String email = tokenService.validateToken(token);
        userService.updatePassword(email, dto.password());
        return ResponseEntity.ok("Password reset successfully");
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<GetUserResponseDto> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(GetUserResponseDto.fromEntity(user));
    }


    @GetMapping
    @Cacheable("users")
    public ResponseEntity<List<GetUserResponseDto>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(GetUserResponseDto.fromEntities(users));
    }


    @DeleteMapping(value = "/{id}")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        userService.delete(id, admin);
        return ResponseEntity.noContent().build();
    }
}
