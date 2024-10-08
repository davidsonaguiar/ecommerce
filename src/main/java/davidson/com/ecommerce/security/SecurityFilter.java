package davidson.com.ecommerce.security;

import davidson.com.ecommerce.exceptions.UnauthorizedException;
import davidson.com.ecommerce.resources.user.User;
import davidson.com.ecommerce.resources.user.UserRespository;
import davidson.com.ecommerce.resources.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            if (token == null) throw new UnauthorizedException("User not authenticated");

            String email = tokenService.validateToken(token);
            UserDetails userDetails = userRespository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("User not authenticated"));

            User user = (User) userDetails;
            if(!user.isActive()) throw new UnauthorizedException("User not authenticated");

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }


    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) return null;
        return token.replace("Bearer ", "");
    }
}
