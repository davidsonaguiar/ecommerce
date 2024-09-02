package davidson.com.ecommerce.exceptions.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        StandardError error = new StandardError();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage("Access Denied");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonError = objectMapper.writeValueAsString(error);
        response.getWriter().write(jsonError);
    }
}
