package davidson.com.ecommerce.resources.user;

import davidson.com.ecommerce.resources.user.dtos.request.SigninRequestDto;
import davidson.com.ecommerce.resources.user.dtos.request.SignupRequestDto;
import davidson.com.ecommerce.resources.user.dtos.response.SigninResponseDto;
import davidson.com.ecommerce.resources.user.dtos.response.SignupResponseDto;
import davidson.com.ecommerce.security.TokenService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody @Valid SigninRequestDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok().body(new SigninResponseDto(token));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto dto) {
        User user = userService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SignupResponseDto.fromEntity(user));
    }

    @PostMapping(value = "/signup/admin")
    public ResponseEntity<SignupResponseDto> signupAdmin(@RequestBody @Valid SignupRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();

        User user = userService.signupAdmin(dto, admin);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SignupResponseDto.fromEntity(user));
    }
}
