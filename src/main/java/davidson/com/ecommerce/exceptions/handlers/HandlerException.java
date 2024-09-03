package davidson.com.ecommerce.exceptions.handlers;

import davidson.com.ecommerce.exceptions.ContentConflictException;
import davidson.com.ecommerce.exceptions.ForbiddenException;
import davidson.com.ecommerce.exceptions.ResourceNotFoundException;
import davidson.com.ecommerce.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardException> handleException(ResourceNotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ContentConflictException.class)
    public ResponseEntity<StandardException> handleException(ContentConflictException exception) {
        HttpStatus status = HttpStatus.CONFLICT;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardException> handleException(UnauthorizedException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardException> handleException(ForbiddenException exception) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardException> handleException(MethodArgumentNotValidException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getAllErrors().stream()
                .map(errorObject -> errorObject.getDefaultMessage())
                .collect(Collectors.joining(", ")));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardException> handleException(Exception exception) {
        exception.printStackTrace();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<StandardException> handleException(NoResourceFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Resource not found");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardException> handleException(NullPointerException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardException error = new StandardException();
        error.setStatus(status.value());
        error.setMessage("Internal server error");

        return ResponseEntity.status(status).body(error);
    }
}
