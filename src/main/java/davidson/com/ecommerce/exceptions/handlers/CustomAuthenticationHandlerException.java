package davidson.com.ecommerce.exceptions.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationHandlerException implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
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
