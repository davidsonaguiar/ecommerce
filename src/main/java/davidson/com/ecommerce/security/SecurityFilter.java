package davidson.com.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import davidson.com.ecommerce.exceptions.ResourceNotFoundException;
import davidson.com.ecommerce.exceptions.handlers.StandardException;
import davidson.com.ecommerce.resources.user.UserRespository;
import davidson.com.ecommerce.resources.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserService userService;
    private final UserRespository userRespository;

    public SecurityFilter(TokenService tokenService, UserService userService, UserRespository userRespository) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.userRespository = userRespository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if (token != null) {
                String email = tokenService.validateToken(token);
                UserDetails user = userRespository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception exception) {
            Integer status = HttpStatus.UNAUTHORIZED.value();

            StandardException error = new StandardException();
            error.setStatus(status);
            error.setMessage("You are not authenticated");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(error);

            response.setStatus(status);
            response.setContentType("application/json");
            response.getWriter().write(json);
        }
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) return null;
        return token.replace("Bearer ", "");
    }
}
